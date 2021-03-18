from bs4 import BeautifulSoup
import pandas
import requests
import json

from geopy.geocoders import Nominatim

regionurl1 = "https://georgiarcc.org/?county=&region="
regionurl2 = "&regional_coordinating_hospital=&search_by_type=&search-by-hospital=&search="
df = pandas.DataFrame()

for i in range(1,11):
    page = requests.get(regionurl1 + str(i) + regionurl2)
    soup = BeautifulSoup(page.content, 'lxml')
    table = soup.find_all('table')[0]
    dfs = pandas.read_html(str(table))
    temp = pandas.DataFrame(dfs[0])
    temp['EMSRegion'] = i
    df = df.append(temp)

df = df.reset_index()
df = df.drop(columns=['index', 'Nedocs', 'Status', 'Updated'])

coordurl1 = "https://georgiarcc.org/?county=&region=&regional_coordinating_hospital="
coordurl2 = "&search_by_type=&search-by-hospital=&search="
a = 65
for i in range (a, 79):
    page = requests.get(coordurl1 + chr(i) + coordurl2)
    soup = BeautifulSoup(page.content, 'lxml')
    table = soup.find_all('table')[0]
    dfs = pandas.read_html(str(table))
    temp = pandas.DataFrame(dfs[0])
    for entry in temp['Hospital']:
        df.loc[df['Hospital'] == entry,'RegCoord'] = chr(i)

specialurl1 = "https://georgiarcc.org/?county=&region=&regional_coordinating_hospital=&search_by_type="
specialurl2 = "&option_by_search="
specialurl3 = "&search-by-hospital=&search="

h_types = {'Adult Trauma Centers': ['Level I', 'Level II', 'Level III', 'Level IV'],
         'Pediatric Trauma Centers': ['Pediatric Level I', 'Pediatric Level II'],
         'Stroke Centers': ['Comprehensive Stroke Center', 'Thrombectomy Capable Stroke Center', 'Primary Stroke Center', 'Remote Treatment Stroke Center'],
         'Emergency Cardiac Care Center': ['Level I ECCC', 'Level II ECCC', 'Level III ECCC'],
         'Neonatal Center Designation': ['Level I Neonatal Center', 'Level II Neonatal Center', 'Level III Neonatal Center'],
         'Maternal Center Designation': ['Level I Maternal Center', 'Level II Maternal Center', 'Level III Maternal Center']}

df['Specialty center'] = [[] for x in range(len(df))]

centers = []

for h_type in h_types:
    for spec in h_types[h_type]:
        page = requests.get(specialurl1 + str(h_type) + specialurl2 + str(spec) + specialurl3)
        centers.append((str(h_type) + "-" + str(spec)))
        soup = BeautifulSoup(page.content, 'lxml')
        table = soup.find_all('table')[0]
        dfs = pandas.read_html(str(table))
        temp = pandas.DataFrame(dfs[0])
        # for h in temp['Hospital']:
        #     df.loc[df['Hospital'] == h, (str(h_type) + "-" + str(spec))] = True
        for h in temp['Hospital']:
            df.loc[df['Hospital'] == h, "Specialty center"] += [(str(h_type) + "-" + str(spec))]
        # if (str(h_type) + "-" + str(spec)) in df.columns:
        #     df[(str(h_type) + "-" + str(spec))] = df[(str(h_type) + "-" + str(spec))].fillna(value=False)

new_centers = {'specialty_centers': centers}
with open('specialty_centers.json', 'w') as outfile:
    json.dump(new_centers, outfile)

df.to_json("extra.json", orient='records', lines=True)
#print(df)

p_a = pandas.read_csv("HospitalInfo.csv")
p_a = p_a.drop(p_a.columns[0],axis=1)
p_a['Street'], p_a['City'], p_a['StateZip'] = p_a['Address'].str.split(',', 2).str
p_a['Zip'] = p_a['StateZip'].str[3:]

#======insert code for lat and long here
geolocator = Nominatim(user_agent="EMS_Mobile_App")
latitudes = []
longitudes = []
for e in p_a['Address']:
    try:
        location = geolocator.geocode(e)
        latitudes.append(location.latitude)
        longitudes.append(location.longitude)
    except:
        latitudes.append(None)
        longitudes.append(None)
p_a['Latitude'] = latitudes
p_a['Longitude'] = longitudes
#=======================================


p_a = p_a.drop(columns=['Address', 'StateZip'])
p_a = p_a.drop(72)
p_a.to_json("HospitalStaticInfo.json", orient='records', lines=True)

p_a['Phone Number'] = p_a['Phone Number'].str.replace(" ", "")
p_a['Phone Number'] = p_a['Phone Number'].str.replace("(", "")
p_a['Phone Number'] = p_a['Phone Number'].str.replace(")", "")
p_a['Phone Number'] = p_a['Phone Number'].str.replace("-", "")
df = df.drop(72)

total = pandas.merge(df, p_a, on='Hospital',how='inner')
total.to_json("Hospital.json", orient='records', lines=True)
