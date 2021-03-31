//
//  HomeVC.swift
//  EMS iOS App
//
//  Created by Ryan Tobin on 11/9/20.
//  Copyright © 2020 JD_0340_EMS. All rights reserved.
//

import UIKit

class HomeVC: UIViewController, UITableViewDelegate, UITableViewDataSource, UIGestureRecognizerDelegate, UISearchBarDelegate {
    
    @IBOutlet var tableView: UITableView!
    var hospitals: Array<HospitalIH>!
    var pinnedList: Array<HospitalIH>!
    var filteredList: Array<HospitalIH>!
    var searchedList: Array<HospitalIH>!
    var searchActive : Bool = false
    var filterActive : Bool = false
    var sortActive : Bool = false
    var appliedFilters : Array<FilterIH>!
    var appliedSort: SortType!
    var thereIsCellTapped = false
    var selectedRowIndex = -1
    let defaults = UserDefaults.standard
    @IBOutlet var searchBar: UISearchBar!
    @IBOutlet var searchBarHeight : NSLayoutConstraint!
    

    override func viewDidLoad() {
        super.viewDidLoad()
        self.navigationController?.navigationBar.barStyle = UIBarStyle.black
        self.searchBar.isHidden = true
        self.searchBar.delegate = self
        self.hospitals = Array<HospitalIH>()
        self.pinnedList = Array<HospitalIH>()
        //self.searchedList = Array<HospitalIH>()
        self.filteredList = Array<HospitalIH>()
        self.tableView.delegate = self
        self.tableView.dataSource = self
        
        self.appliedFilters = Array<FilterIH>()
        
        // initial load of data
        buildHospitalList()
        
        
        
        // swipe to refresh data
        tableView.refreshControl = UIRefreshControl()
        tableView.refreshControl?.addTarget(self, action: #selector(buildHospitalList), for: .valueChanged)
    }
    func showSearchBar() {
            self.searchBar.isHidden = false
            self.searchBarHeight.constant = 56
            self.searchBar.becomeFirstResponder()
            UIView.animate(withDuration: 0.25, animations: {
                 self.view.layoutIfNeeded()
            }, completion: nil)
        }

    func hideSearchBar() {
        self.searchBar.resignFirstResponder()
        self.searchBarHeight.constant = 0
        UIView.animate(withDuration: 0.25, animations: {
            self.view.layoutIfNeeded()
        }, completion: { _ in
            self.searchBar.isHidden = true
        })
    }

    @IBAction func searchPressed(_ sender: UIButton) {
        if self.searchBar.isHidden {
            self.showSearchBar()
        } else {
            self.hideSearchBar()
        }
    }
    func searchBarTextDidBeginEditing(_ searchBar: UISearchBar) {
        searchActive = true;
    }

    func searchBarTextDidEndEditing(_ searchBar: UISearchBar) {
        searchActive = false;
    }

    func searchBarCancelButtonClicked(_ searchBar: UISearchBar) {
        self.searchBar.text = nil
        self.hideSearchBar()
        searchActive = false;
        tableView.reloadData()
    }

    func searchBarSearchButtonClicked(_ searchBar: UISearchBar)  {
        self.hideSearchBar()
        searchActive = false;
    }

    // TODO: Filter hospitals based on searchText
    func searchBar(_ searchBar: UISearchBar, textDidChange searchText: String) {
        if (filterActive) {
            searchedList = filteredList.filter({ (hos) -> Bool in
                let tmp: String = hos.name
                return tmp.range(of: searchText, options: .caseInsensitive) != nil
                })
        } else {
        searchedList = hospitals.filter({ (hos) -> Bool in
            let tmp: String = hos.name
            return tmp.range(of: searchText, options: .caseInsensitive) != nil
            })
        }
           
        tableView.reloadData()
    }
    
    
    func handleFilter() {
        if (!filterActive) {
            filteredList = hospitals
        }
        for filter in appliedFilters {
            switch filter.field {
            case .county:
                var newHospitals = Array<HospitalIH>()
                //iterate through values to see if multiple counties were selected
                for iCounty in filter.values {
                    newHospitals.append(contentsOf: self.filteredList.filter({ (hos) -> Bool in
                        let tmp: String = hos.county
                        return tmp == iCounty
                        }))
                }
                filteredList = newHospitals
                filterActive = true
            case .emsRegion:
                var newHospitals = Array<HospitalIH>()
                //iterate through values to see if multiple regions were selected
                for iRegion in filter.values {
                    newHospitals.append(contentsOf: self.filteredList.filter({ (hos) -> Bool in
                        let tmp: String = hos.regionNumber
                        return tmp == iRegion
                        }))
                }
                filteredList = newHospitals
                filterActive = true
            case .rch:
                var newHospitals = Array<HospitalIH>()
                //iterate through values to see if multiple rch's were selected
                for iRCH in filter.values {
                    newHospitals.append(contentsOf: self.filteredList.filter({ (hos) -> Bool in
                        let tmp: String = hos.rch
                        return tmp == iRCH
                        }))
                }
                filteredList = newHospitals
                filterActive = true
            case .type:
                var newHospitals = Array<HospitalIH>()
                for speCenter in filter.values {
                    let type = HospitalType(rawValue: speCenter)
                    newHospitals.append(contentsOf: self.filteredList.filter({ (hos) -> Bool in
                        let tmp: Array<HospitalType> = hos.specialtyCenters
                        return tmp.contains(type!)
                        }))
                }
                filteredList = newHospitals
                filterActive = true
            default:
                break
            }
            
        }
    }
 
    
    func handleSort() {
        sortActive = true
        //print(appliedSort)
        switch appliedSort {
        case .az:
            print(appliedSort.rawValue)
            hospitals.sort{
                $0.name < $1.name
            }
        case .distance:
            hospitals.sort{
                $0.distance < $1.distance
            }
        case .nedocs:
            hospitals.sort{
                $0.nedocsScore < $1.nedocsScore
            }
        default:
            break
        }
        tableView.reloadData()
    }
    
    func getInitialPinned() {
        if let data = UserDefaults.standard.value(forKey:"pinnedHospitals") as? Data {
            let loadedHospitalList = try! PropertyListDecoder().decode(Array<Hospital>.self, from: data)
            let range = loadedHospitalList.count
            self.pinnedList = Array<HospitalIH>()
            for num in 0..<range {
                let hosp = HospitalIH(name: loadedHospitalList[num].name, nedocsScore: loadedHospitalList[num].nedocsScore, specialtyCenters: loadedHospitalList[num].specialtyCenters, distance: loadedHospitalList[num].distance, lat: loadedHospitalList[num].lat ?? 0, long: loadedHospitalList[num].long ?? 0, hasDiversion: loadedHospitalList[num].hasDiversion, diversions:loadedHospitalList[num].diversions, address: loadedHospitalList[num].address, phoneNumber: loadedHospitalList[num].phoneNumber, regionNumber: loadedHospitalList[num].regionNumber, county: loadedHospitalList[num].county, rch: loadedHospitalList[num].rch)
                if (!self.pinnedList.contains(hosp)) {
                    self.pinnedList.append(hosp)
                }
            }
            for pinned in self.pinnedList {
                //print(pinnedList.count)
                if let index = hospitals.index(where: {$0.name == pinned.name}) {
                    hospitals.remove(at: index);
                    hospitals.insert(pinned, at: 0);
                    pinned.isFavorite = true
                }

            }
 
        }
        
    }

    // helper method to build Hospital List from data
    @objc func buildHospitalList() {
        let tasker = HospitalsTasker()
        tasker.getAllHospitals(failure: {
            print("Failure")
        }, success: { (hospitals) in
            guard var hospitals = hospitals else {
                self.hospitals = Array<HospitalIH>()
                DispatchQueue.main.async {
                    self.tableView.reloadData()
                }
                return
            }
            
            self.hospitals = hospitals
            self.getInitialPinned()
            DispatchQueue.main.async {
                self.tableView.reloadData()
            }
            
            // Dismiss the refresh control
            DispatchQueue.main.async {
                self.tableView.refreshControl?.endRefreshing()
            }
        })
        
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        if (searchActive) {
            return searchedList.count
        } else if (filterActive) {
            return filteredList.count
        }
        else {
            return hospitals.count
        }
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let hospital:HospitalIH
        let cell = tableView.dequeueReusableCell(withIdentifier: "hospitalCell", for: indexPath) as! HomeTableViewCell
        if (searchActive) {
            hospital = self.searchedList[indexPath.row]
        } else if (filterActive) {
            hospital = self.filteredList[indexPath.row]
        } else {
            hospital = self.hospitals[indexPath.row]
        }
        
        cell.hospitalName.text = hospital.name
        if (hospital.isFavorite) {
            cell.favoritePin.setImage(UIImage(named: "FilledFavoritePin"), for: .normal)
        } else {
            cell.favoritePin.setImage(UIImage(named: "OutlineFavoritePin"), for: .normal)
        }
        cell.nedocsView.layer.cornerRadius = 5.0
        cell.nedocsView.backgroundColor = hospital.nedocsScore.getNedocsColor()
        cell.nedocsLabel.text = hospital.nedocsScore.rawValue
        if (hospital.nedocsScore == NedocsScore(rawValue: "Overcrowded")) {
            cell.nedocsLabel.font = cell.nedocsLabel.font.withSize(11)
        } else {
            cell.nedocsLabel.font = cell.nedocsLabel.font.withSize(16)
        }
        if (hospital.hasDiversion) {
            // show diversion icon when diversion
            cell.diversionIcon?.isHidden = false
            cell.diversionIconWidth.constant = 25
            cell.diversionIconLeading.constant = 10
            
            // show expanded info about diversion when diversion
            cell.diversionsHolder.isHidden = false
            cell.expandedDiversionIconLabel.isHidden = false

            let currDiversionIcon: UIImage?
            switch hospital.diversions.count {
            case 1:
                currDiversionIcon = UIImage(named: "Warning1Icon")
            case 2:
                currDiversionIcon = UIImage(named: "Warning2Icon")
            case 3:
                currDiversionIcon = UIImage(named: "Warning3Icon")
            case 4:
                currDiversionIcon = UIImage(named: "Warning4Icon")
            case 5:
                currDiversionIcon = UIImage(named: "Warning5Icon")
            case 6:
                currDiversionIcon = UIImage(named: "Warning6Icon")
            default:
                currDiversionIcon = UIImage(named: "WarningIcon")
            }
            cell.diversionIcon?.image = currDiversionIcon
            cell.expandedDiversionIcon?.image = currDiversionIcon
            cell.expandedDiversionIconLabel.text = ""
            for i in 0...(hospital.diversions.count - 1) {
                if (i != hospital.diversions.count - 1) {
                    cell.expandedDiversionIconLabel.text! += hospital.diversions[i] + "\n"
                } else {
                    cell.expandedDiversionIconLabel.text! += hospital.diversions[i]
                }
            }
            
        } else {
            // hide diversion icon when no diversion
            cell.diversionIcon?.isHidden = true
            cell.diversionIconWidth.constant = 0
            cell.diversionIconLeading.constant = 0
            
            // hide expanded info about diversion when no diversion
            cell.diversionsHolder.isHidden = true
            cell.expandedDiversionIconLabel.isHidden = true
        }
        
        // default hide all hospital types
        cell.hospitalTypeIcon1.isHidden = true
        cell.hospitalTypeIcon2.isHidden = true
        cell.hospitalTypeIcon3.isHidden = true
        cell.hospitalTypeHolder1.isHidden = true
        cell.hospitalTypeHolder2.isHidden = true
        cell.hospitalTypeHolder3.isHidden = true

        if (hospital.specialtyCenters.count != 0) {
            for i in 0...(hospital.specialtyCenters.count - 1) {
                var currHospitalTypeIcon: UIImageView? = nil
                var currHospitalTypeHolder: UIStackView? = nil
                var currExpandedHospitalTypeIcon: UIImageView? = nil
                var currHospitalTypeLabel: UILabel? = nil

                switch i {
                case 0:
                    currHospitalTypeIcon = cell.hospitalTypeIcon1
                    currHospitalTypeHolder = cell.hospitalTypeHolder1
                    currExpandedHospitalTypeIcon = cell.expandedHospitalTypeIcon1
                    currHospitalTypeLabel = cell.hospitalTypeLabel1
                case 1:
                    currHospitalTypeIcon = cell.hospitalTypeIcon2
                    currHospitalTypeHolder = cell.hospitalTypeHolder2
                    currExpandedHospitalTypeIcon = cell.expandedHospitalTypeIcon2
                    currHospitalTypeLabel = cell.hospitalTypeLabel2
                case 2:
                    currHospitalTypeIcon = cell.hospitalTypeIcon3
                    currHospitalTypeHolder = cell.hospitalTypeHolder3
                    currExpandedHospitalTypeIcon = cell.expandedHospitalTypeIcon3
                    currHospitalTypeLabel = cell.hospitalTypeLabel3
                default:
                    break
                }
                
                if (currHospitalTypeIcon != nil && currExpandedHospitalTypeIcon != nil && currHospitalTypeLabel != nil) {
                    // unhide hospital type icon
                    currHospitalTypeIcon?.isHidden = false
                    
                    // unhide expanded info about hospital type
                    currHospitalTypeHolder?.isHidden = false
                    
                    currHospitalTypeIcon?.image = hospital.specialtyCenters[i].getHospitalIcon()
                    currExpandedHospitalTypeIcon?.image = hospital.specialtyCenters[i].getHospitalIcon()
                    currHospitalTypeLabel?.text = hospital.specialtyCenters[i].getHospitalDisplayString()
                }
            }
        }
        
        cell.distanceLabel.text = "\(String(hospital.distance)) mi"
        cell.address.attributedText = NSAttributedString(string: hospital.address, attributes:
            [.underlineStyle: NSUnderlineStyle.single.rawValue])
        cell.address.textColor = UIColor.blue
        cell.phoneNumber.attributedText = NSAttributedString(string: hospital.phoneNumber, attributes:
            [.underlineStyle: NSUnderlineStyle.single.rawValue])
        cell.phoneNumber.textColor = UIColor.blue
        cell.countyLabel.text = "\(String(hospital.county)) County - EMS Region \(String(hospital.regionNumber))"
        cell.rchLabel.text = "Regional Coordination Hospital \(String(hospital.rch))"
    
        return cell
    }

    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        if indexPath.row == selectedRowIndex && thereIsCellTapped {
            let hospital:HospitalIH
            if (searchActive) {
                hospital = self.searchedList[indexPath.row]
            } else if (filterActive) {
                hospital = self.filteredList[indexPath.row]
            } else {
                hospital = self.hospitals[indexPath.row]
            }
            if hospital.hasDiversion {
                // calculate the height of the expanded cell based on the number of diversions and hospital types
                return CGFloat(179 + 28 * (hospital.specialtyCenters.count) + (6 * (hospital.diversions.count - 1)))
            } else {
                // calculate the height of the expanded cell based on the number of hospital types
                return CGFloat(154 + 28 * hospital.specialtyCenters.count)
            }
        }
        return 90
    }
    
    @IBAction func expandButton(_ sender: UIButton) {
        guard let cell = sender.superview?.superview as? HomeTableViewCell else {
            return
        }

        guard let indexPath = tableView.indexPath(for: cell) else {
            return
        }
        
        cell.expandButton.isHidden = true
        cell.minimizeButton.isHidden = false
        
        if self.selectedRowIndex != -1 {
            self.tableView.cellForRow(at: NSIndexPath(row: self.selectedRowIndex, section: 0) as IndexPath)?.backgroundColor = UIColor.white
            guard let prevCell = self.tableView.cellForRow(at: NSIndexPath(row: self.selectedRowIndex, section: 0) as IndexPath) as? HomeTableViewCell else {
                return
            }
            prevCell.expandButton.setImage(UIImage(named:"ArrowIcon"), for: .normal)
            
        }

        if selectedRowIndex != indexPath.row {
            self.thereIsCellTapped = true
            self.selectedRowIndex = indexPath.row
            cell.expandButton.setImage(UIImage(named:"ArrowIconUp"), for: .normal)
        } else {
            cell.minimizeButton.isHidden = true
            cell.expandButton.isHidden = false
            self.thereIsCellTapped = false
            self.selectedRowIndex = -1
            cell.expandButton.setImage(UIImage(named:"ArrowIcon"), for: .normal)
        }
        self.tableView.beginUpdates()
        self.tableView.endUpdates()
    }
    
    
    @IBAction func favoritePin(_ sender: UIButton) {
        
        guard let cell = sender.superview?.superview as? HomeTableViewCell else {
            return
        }

        guard let indexPath = tableView.indexPath(for: cell) else {
            return
        }
        
        guard var newIndexPath = tableView.indexPath(for: cell) else {
            return
        }
        if (pinnedList.isEmpty) {
            newIndexPath = IndexPath(item: 0, section: 0)
        } else {
            newIndexPath = IndexPath(item: pinnedList.count, section: 0)
        }
        let pinnedHospital : HospitalIH
        if (searchActive) {
            pinnedHospital = self.searchedList[indexPath.row]
        } else if (filterActive) {
            pinnedHospital = self.filteredList[indexPath.row]
        } else {
            pinnedHospital = self.hospitals[indexPath.row]
        }

        if (!pinnedList.contains(pinnedHospital)) {
            //!favoritePinTapped) {
            let myImage = UIImage(named: "FilledFavoritePin")
            cell.favoritePin.setImage(myImage, for: .normal)
            self.pinnedList.append(pinnedHospital)
            self.hospitals.remove(at:indexPath.row)
            self.hospitals.insert(pinnedHospital, at:0)
            self.tableView.moveRow(at: indexPath, to: IndexPath(item: 0, section: 0))
            pinnedHospital.isFavorite = true
            //let hospital = hospitals.remove(at:indexPath.row)
            //hospitals.insert(hospital, at: 0)
        } else {
            let myImage = UIImage(named: "OutlineFavoritePin")
            cell.favoritePin.setImage(myImage, for: .normal)
            guard let pinIndex = pinnedList.firstIndex(of:pinnedHospital) else {
                return
            }
            pinnedList.remove(at:pinIndex)
            self.hospitals.remove(at:indexPath.row)
            self.hospitals.insert(pinnedHospital, at: pinnedList.count)
            self.tableView.moveRow(at: indexPath, to: newIndexPath)
            pinnedHospital.isFavorite = false
        }
        self.savePinned(hospitals:self.hospitals, pinned: self.pinnedList)
        self.tableView.beginUpdates()
        self.tableView.endUpdates()
    }
    
    func savePinned(hospitals: Array<HospitalIH>, pinned: Array<HospitalIH>) {
        
        var newList = Array<Hospital>()
        for hos in pinned {
            let pinnedHos = Hospital(name: hos.name,
                                 nedocsScore: hos.nedocsScore,
                                 specialtyCenters: hos.specialtyCenters,
                                 distance: hos.distance,
                                 lat: hos.lat,
                                 long: hos.long,
                                 hasDiversion: hos.hasDiversion,
                                 diversions:hos.diversions,
                                 address:hos.address,
                                 phoneNumber: hos.phoneNumber,
                                 regionNumber: hos.regionNumber,
                                 county: hos.county,
                                 rch: hos.rch)
            newList.append(pinnedHos)
        }
        UserDefaults.standard.set(try? PropertyListEncoder().encode(newList), forKey:"pinnedHospitals")
        
    }
    
}
