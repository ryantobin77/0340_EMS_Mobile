from bs4 import BeautifulSoup
import pandas
import requests

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
df = df.drop(columns=['index', 'County', 'Nedocs', 'Status', 'Updated'])

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

for h_type in h_types:
    for spec in h_types[h_type]:
        page = requests.get(specialurl1 + str(h_type) + specialurl2 + str(spec) + specialurl3)
        soup = BeautifulSoup(page.content, 'lxml')
        table = soup.find_all('table')[0]
        dfs = pandas.read_html(str(table))
        temp = pandas.DataFrame(dfs[0])
        for h in temp['Hospital']:
            df.loc[df['Hospital'] == h, (str(h_type) + "-" + str(spec))] = True
        if (str(h_type) + "-" + str(spec)) in df.columns:
            df[(str(h_type) + "-" + str(spec))] = df[(str(h_type) + "-" + str(spec))].fillna(value=False)

# max_spec = 0
# max_name = "Test"
# for row in df[1:]:
#     curr = 0
#     for col in df.columns[3:]:
#         curr = curr + 1 if df[row, col] else curr
#     if curr > max_spec:
#         max_spec = curr
#         max_name = df[row, "Hospital"]

# print("max specialties: " + str(max_spec))
# print("name: " + str(max_name))

# names = pandas.DataFrame(df['Hospital'])
# names.to_csv("HospitalInfo.csv")



df.to_json("extra.json", orient='records', lines=True)
#print(df)
