//
//  HomeVC.swift
//  EMS iOS App
//
//  Created by Ryan Tobin on 11/9/20.
//  Copyright © 2020 JD_0340_EMS. All rights reserved.
//

import UIKit

class HomeVC: UIViewController, UITableViewDelegate, UITableViewDataSource, UIGestureRecognizerDelegate, FilterSelectorDelegate, UISearchBarDelegate, SortSelectorDelegate {
    @IBOutlet var tableView: UITableView!
    @IBOutlet var appliedFiltersView: UIStackView!
    @IBOutlet var clearAllFiltersButton: UIButton!
    @IBOutlet var bottomBarScrollView: UIScrollView!
    @IBOutlet var searchBar: UISearchBar!
    @IBOutlet var searchBarHeight : NSLayoutConstraint!
    var hospitals: Array<HospitalIH>!
    var pinnedList: Array<HospitalIH>!
    
    var filteredList: Array<HospitalIH>!
    var searchedList: Array<HospitalIH>!
    var sortedList: Array<HospitalIH>!
    var searchActive : Bool = false
    var filterActive : Bool = false
    var sortActive : Bool = false
    var appliedSort: SortType!
    var popupHandler = MenuPopupHandler()
    var sortSelectorView = SortSelectorView()
    let sortSelectorViewHeight: CGFloat = 177
    var appliedFilters: Array<FilterIH>!
    var filterCards: NSMutableArray!
    var thereIsCellTapped = false
    var selectedRowIndex = -1
    
    let GCC_NUMBER = "4046166440"
    
    override func viewDidLoad() {
        super.viewDidLoad()
        self.navigationController?.navigationBar.barStyle = UIBarStyle.black
        self.searchBar.isHidden = true
        self.searchBar.delegate = self
        self.tableView.delegate = self
        self.tableView.dataSource = self
        self.pinnedList = Array<HospitalIH>()
        self.appliedFilters = Array<FilterIH>()
                
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
        if !self.searchBar.isHidden && self.searchBar.searchTextField.text!.isEmpty {
            self.hideSearchBar()
        } else if self.searchBar.isHidden {
            self.showSearchBar()
        } else {
            self.searchBar.searchTextField.resignFirstResponder()
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
        if self.searchBar.searchTextField.text!.isEmpty {
            self.hideSearchBar()
        } else {
            self.searchBar.searchTextField.resignFirstResponder()
        }
        searchActive = false
    }
    
    // TODO: Filter hospitals based on searchText
    func searchBar(_ searchBar: UISearchBar, textDidChange searchText: String) {
        print(searchText)
        if searchText == "" {
            searchActive = false
            tableView.reloadData()
        } else {
            searchActive = true
            if (filterActive) {
                searchedList = filteredList.filter({ (hos) -> Bool in
                    let tmp: String = hos.name
                    return tmp.range(of: searchText, options: .caseInsensitive) != nil
                    })
            } else if(sortActive){
                searchedList = sortedList.filter({ (hos) -> Bool in
                    let tmp: String = hos.name
                    return tmp.range(of: searchText, options: .caseInsensitive) != nil
                    })
            }else {
            searchedList = hospitals.filter({ (hos) -> Bool in
                let tmp: String = hos.name
                return tmp.range(of: searchText, options: .caseInsensitive) != nil
                })
            }
            tableView.reloadData()
        }
        
    }

    // helper method to build Hospital List from data
    @objc func buildHospitalList() {
        let tasker = HospitalsTasker()
        tasker.getAllHospitals(failure: {
            print("Failure")
        }, success: { (hospitals) in
            guard let hospitals = hospitals else {
                self.hospitals = Array<HospitalIH>()
                DispatchQueue.main.async {
                    self.tableView.reloadData()
                }
                return
            }
            
            tasker.getHospitalDistances(hospitals: hospitals, finished: { (hospitals) in
                guard var hospitals = hospitals else {
                    self.hospitals = Array<HospitalIH>()
                    DispatchQueue.main.async {
                        self.tableView.reloadData()
                        self.tableView.refreshControl?.endRefreshing()
                    }
                    return
                }
                // Retrieve pinned hospitals in new list
                var newPinned = [HospitalIH]()
                for pinned in self.pinnedList {
                    for hospital in hospitals {
                        if (hospital.name == pinned.name) {
                            newPinned.append(hospital)
                        }
                    }
                }
                // Repin hospitals
                for pinned in newPinned {
                    if let index = hospitals.firstIndex(of: pinned) {
                        hospitals.remove(at: index)
                    }
                    hospitals.insert(pinned, at: 0)
                    pinned.isFavorite = true
                }
                self.pinnedList = newPinned
                self.hospitals = hospitals
                DispatchQueue.main.async {
                    self.tableView.reloadData()
                    self.tableView.refreshControl?.endRefreshing()
                }
            })
        })
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        if (hospitals == nil) {
            return -1
        }
        if ((sortActive && searchActive) || (filterActive && searchActive)) {
            return searchedList.count
        } else if (sortActive) {
            return sortedList.count
        } else if (searchActive) {
            return searchedList.count
        } else if (filterActive) {
            return filteredList.count
        }
        else {
            return hospitals.count
        }
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        var hospital: HospitalIH
        let cell = tableView.dequeueReusableCell(withIdentifier: "hospitalCell", for: indexPath) as! HomeTableViewCell
        if ((sortActive && searchActive) || (filterActive && searchActive)) {
            hospital = self.searchedList[indexPath.row]
        } else if (sortActive) {
            hospital = self.sortedList[indexPath.row]
        } else if (searchActive) {
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
        
        if (hospital.distance == -1.0) {
            cell.distanceLabel.text = "-- mi"
        } else {
            cell.distanceLabel.text = "\(String(hospital.distance)) mi"
        }
        
        cell.address.titleLabel!.numberOfLines = 2
        cell.address.setTitle(hospital.address, for: .normal)
        cell.address.setAttributedTitle(NSAttributedString(string: hospital.address, attributes:
            [.underlineStyle: NSUnderlineStyle.single.rawValue]), for: .normal)
        cell.address.setTitleColor(.blue, for: .normal)
        cell.address.addTarget(self, action: #selector(mapToHospital(_:)), for: .touchUpInside)
        
        let formattedPhone = self.applyPatternOnNumbers(number: hospital.phoneNumber, pattern: "(###) ###-####", replacementCharacter: "#")
        cell.phoneNumber.setTitle(formattedPhone, for: .normal)
        cell.phoneNumber.setAttributedTitle(NSAttributedString(string: formattedPhone, attributes:
            [.underlineStyle: NSUnderlineStyle.single.rawValue]), for: .normal)
        cell.phoneNumber.setTitleColor(.blue, for: .normal)
        cell.phoneNumber.addTarget(self, action: #selector(callHospital(_:)), for: .touchUpInside)
        cell.phoneIcon.addTarget(self, action: #selector(callHospital(_:)), for: .touchUpInside)
        cell.countyLabel.text = "\(String(hospital.county)) County - EMS Region \(String(hospital.regionNumber))"
        cell.rchLabel.text = "Regional Coordination Hospital \(String(hospital.rch))"
        
        let dateFormatter = DateFormatter()
        dateFormatter.locale = Locale(identifier: "en_US_POSIX") // set locale to reliable US_POSIX
        dateFormatter.dateFormat = "MM/dd/yyyy h:mm:ss aa"
        let formattedLastUpdated = dateFormatter.string(from: hospital.lastUpdated)
        cell.lastUpdatedLabel.text = "Updated \(String(formattedLastUpdated))"
    
        return cell
    }
    
    func applyPatternOnNumbers(number: String, pattern: String, replacementCharacter: Character) -> String {
        var pureNumber = number.replacingOccurrences(of: "[^0-9]", with: "", options: .regularExpression)
        for index in 0 ..< pattern.count {
            guard index < pureNumber.count else { return pureNumber }
            let stringIndex = String.Index(utf16Offset: index, in: pattern)
            let patternCharacter = pattern[stringIndex]
            guard patternCharacter != replacementCharacter else { continue }
            pureNumber.insert(patternCharacter, at: stringIndex)
        }
        return pureNumber
    }


    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        if indexPath.row == selectedRowIndex && thereIsCellTapped {
            var hospital:HospitalIH
            if ((sortActive && searchActive) || (filterActive && searchActive)) {
                hospital = self.searchedList[indexPath.row]
            } else if (sortActive) {
                hospital = self.sortedList[indexPath.row]
            } else if (searchActive) {
                hospital = self.searchedList[indexPath.row]
            } else if (filterActive) {
                hospital = self.filteredList[indexPath.row]
            } else {
                hospital = self.hospitals[indexPath.row]
            }
            if hospital.hasDiversion {
                // calculate the height of the expanded cell based on the number of diversions and hospital types
                return CGFloat(192 + 28 * (hospital.specialtyCenters.count) + (10 * (hospital.diversions.count - 1)))
            } else {
                // calculate the height of the expanded cell based on the number of hospital types
                return CGFloat(167 + 28 * hospital.specialtyCenters.count)
            }
        }
        return 90
    }
    
    func callPhoneNumber(number: String) {
        if let url = URL(string: "tel://\(number)"), UIApplication.shared.canOpenURL(url) {
            UIApplication.shared.open(url, options: [:], completionHandler: nil)
        }
    }
    
    @IBAction func callGCC(_ sender: UIButton) {
        self.callPhoneNumber(number: self.GCC_NUMBER)
    }
    
    @objc func callHospital(_ sender: UIButton) {
        guard let cell = sender.superview?.superview?.superview?.superview?.superview as? HomeTableViewCell else {
            return
        }
        guard let indexPath = tableView.indexPath(for: cell) else {
            return
        }
        guard let number = self.hospitals[indexPath.row].phoneNumber else {
            return
        }
        self.callPhoneNumber(number: number)
    }
    
    @objc func mapToHospital(_ sender: UIButton) {
        guard let cell = sender.superview?.superview?.superview?.superview as? HomeTableViewCell else {
            return
        }
        guard let indexPath = tableView.indexPath(for: cell) else {
            return
        }
        guard var address = self.hospitals[indexPath.row].address else {
            return
        }
        address = address.lowercased().replacingOccurrences(of: " ", with: "+")
        let urlStr = "http://maps.apple.com/?address=\(address)"
        guard let url =  URL(string: urlStr) else {
            print(urlStr)
            return
        }
        UIApplication.shared.open(url, options: [:], completionHandler: nil)
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
            
        var pinnedHospital : HospitalIH
        if (sortActive) {
            pinnedHospital = self.sortedList[indexPath.row]
        } else if (searchActive) {
            pinnedHospital = self.searchedList[indexPath.row]
        } else if (filterActive) {
            pinnedHospital = self.filteredList[indexPath.row]
        } else {
            pinnedHospital = self.hospitals[indexPath.row]
        }
        //print("PINNED HOSPITAL ",pinnedHospital.name)
        //pinnedList.append(pinnedHospital)
        
        
        
        //print(pinnedList)
        
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
            self.hospitals.insert(pinnedHospital, at: pinnedList.count + 1)
            self.tableView.moveRow(at: indexPath, to: newIndexPath)
            pinnedHospital.isFavorite = false
        }
        //print("PINNED HOSPTAL ",pinnedList.enumerated())
        self.tableView.beginUpdates()
        self.tableView.endUpdates()
    }
    
    func handlePins() {
        var ind = 0
        for pinnedHospital in self.pinnedList {
            guard let pinIndex = pinnedList.firstIndex(of:pinnedHospital) else {
                return
            }
            if (sortActive) {
                self.sortedList.remove(at:pinIndex)
                self.sortedList.insert(pinnedHospital, at: ind)
            } else if (filterActive) {
                self.filteredList.remove(at:pinIndex)
                self.filteredList.insert(pinnedHospital, at: ind)
            }
            ind += 1
        }
    }
    // sends data to FilterSelectorViewController
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if segue.destination is FilterSelectorViewController {
            let vc = segue.destination as? FilterSelectorViewController
            vc?.delegate = self
            vc?.appliedFilters = appliedFilters
        }
    }
    
    // callback when new filter is applied
    func onFilterSelected(_ appliedFilters: [FilterIH]?) {
        self.appliedFilters = appliedFilters
        
        // Hide Clear All button if there are no filters applied
        if appliedFilters!.count > 0 {
            self.clearAllFiltersButton.setTitle("Clear All", for: .normal)
        } else {
            self.clearAllFiltersButton.setTitle("", for: .normal)
        }
        
        addFilterCards();
        
        //TODO: call filter method here
        handleFilter()
    }
    
    @objc
    func removeFilter(_ sender: FilterButton) {
        if let index = self.appliedFilters.firstIndex(where: {$0.field == sender.field && $0.value == sender.value}) {
            self.appliedFilters.remove(at: index)
            self.appliedFiltersView.subviews[index].removeFromSuperview()
        }
        
        if self.appliedFilters.count == 0 {
            self.clearAllFiltersButton.setTitle("", for: .normal)
            //filterActive = false
        } else {
            //filterActive = true
        }
        
//        // TODO: Update content size of scrollview
//        self.appliedFiltersView.translatesAutoresizingMaskIntoConstraints = false
//        let appliedFiltersViewWidth = self.appliedFiltersView.frame.width
//        // Larger than it should be
//        let filterCardWidth = sender.superview!.frame.width
//        self.appliedFiltersView.widthAnchor.constraint(equalToConstant: appliedFiltersViewWidth - filterCardWidth).isActive = true
//        bottomBarScrollView.contentSize = CGSize(width: Int(bottomBarScrollView.contentSize.width) - Int(filterCardWidth), height: 55)
        
        // TODO: call filter method here
        filterActive = false
        handleFilter()
    }
    
    @IBAction func clearAllFilters(_ sender: UIButton) {
        self.appliedFiltersView.subviews.forEach({ $0.removeFromSuperview() })
        self.clearAllFiltersButton.setTitle("", for: .normal)
        self.appliedFilters = Array<FilterIH>()
        
        // update content size of scrollview
        self.appliedFiltersView.translatesAutoresizingMaskIntoConstraints = false
        self.appliedFiltersView.widthAnchor.constraint(equalToConstant: 0).isActive = true
        bottomBarScrollView.contentSize = CGSize(width: 0, height: 55)
        
        // TODO: call filter method here
        filterActive = false
        handleFilter()
    }
    
    func addFilterCards() {
        // remove all existing filter cards before adding
        self.appliedFiltersView.subviews.forEach({ $0.removeFromSuperview() })
            
        if self.appliedFilters.count > 0 {
            var appliedFiltersViewWidth = 0
            for i in 0...(self.appliedFilters.count - 1) {
                let filter: FilterIH = self.appliedFilters[i]
                if let filterCard = Bundle.main.loadNibNamed("FilterCard", owner: nil, options: nil)!.first as? FilterCard {
                    switch filter.field {
                    case .type:
                        filterCard.valueLabel.text = HospitalType(rawValue: filter.value)!.getHospitalDisplayString()
                    case .emsRegion:
                        filterCard.valueLabel.text = "Region " + filter.value
                    case .county:
                        filterCard.valueLabel.text = filter.value
                    case .rch:
                        filterCard.valueLabel.text = "Regional Coordinating Hospital " + filter.value
                    default:
                        filterCard.valueLabel.text = filter.value
                    }

                    filterCard.removeButton.field = filter.field
                    filterCard.removeButton.value = filter.value
                    filterCard.removeButton.addTarget(self, action: #selector(self.removeFilter(_:)), for: .touchUpInside)
                    self.appliedFiltersView.addArrangedSubview(filterCard)
                    
                    filterCard.translatesAutoresizingMaskIntoConstraints = false
                    let filterCardWidth = filterCard.removeButton.frame.width + filterCard.valueLabel.intrinsicContentSize.width + 12
                    filterCard.widthAnchor.constraint(equalToConstant: filterCardWidth).isActive = true
                    filterCard.heightAnchor.constraint(equalToConstant: 22).isActive = true
                    
                    appliedFiltersViewWidth += Int(filterCardWidth) + 10
                }
            }
                
            self.appliedFiltersView.translatesAutoresizingMaskIntoConstraints = false
            self.appliedFiltersView.widthAnchor.constraint(equalToConstant: CGFloat(appliedFiltersViewWidth)).isActive = true
            bottomBarScrollView.contentSize = CGSize(width: appliedFiltersViewWidth, height: 55)
        }
    }
    
    @IBAction func onSortClicked(_ sender: UIBarButtonItem) {

           sortSelectorView.delegate = self

           popupHandler.displayPopup(sortSelectorView, sortSelectorViewHeight)

       }

   func onSortSelected(_ sort: SortType?) {
    popupHandler.handleDismiss()
    appliedSort = sort
    handleSort()
   }
    
    func handleSort() {
            //var h = Array<HospitalIH>()
            if (filterActive) {
                sortedList = filteredList
            } else if (searchActive){
                sortedList = searchedList
            } else {
                sortedList = hospitals
            }
            sortActive = true
            print("SORT",appliedSort)
            switch appliedSort {
            case .az:
                //print(appliedSort.rawValue)
                sortedList.sort{
                    $0.name < $1.name
                }
            case .distance:
                sortedList.sort{
                    $0.distance < $1.distance
                }
            case .nedocs:
                sortedList.sort{
                    $0.nedocsScore < $1.nedocsScore
                }
            default:
                sortedList.sort{
                    $0.name < $1.name
                }
            }
        if (pinnedList.count > 0) {
            handlePins()
        }
            tableView.reloadData()
        }
    
    func handleFilter() {
            if (!filterActive) {
                filteredList = hospitals
            }
            if appliedFilters.count > 0 {
                for filter in appliedFilters {
                    print(filter.value)
                    switch filter.field {
                    case .county:
                        var newHospitals = Array<HospitalIH>()
                        newHospitals.append(contentsOf: self.filteredList.filter({ (hos) -> Bool in
                            let tmp: String = hos.county
                            return tmp == filter.value
                            }))
                        filteredList = newHospitals
                        filterActive = true
                    case .emsRegion:
                        var newHospitals = Array<HospitalIH>()
                        newHospitals.append(contentsOf: self.filteredList.filter({ (hos) -> Bool in
                            let tmp: String = hos.regionNumber
                            return tmp == filter.value
                            }))
                        filteredList = newHospitals
                        filterActive = true
                    case .rch:
                        var newHospitals = Array<HospitalIH>()
                        newHospitals.append(contentsOf: self.filteredList.filter({ (hos) -> Bool in
                            let tmp: String = hos.rch
                            return tmp == filter.value
                            }))
                        filteredList = newHospitals
                        filterActive = true
                    case .type:
                        var newHospitals = Array<HospitalIH>()
                        let type = HospitalType(rawValue: filter.value)
                        newHospitals.append(contentsOf: self.filteredList.filter({ (hos) -> Bool in
                            let tmp: Array<HospitalType> = hos.specialtyCenters
                            return tmp.contains(type!)
                            }))
                        filteredList = newHospitals
                        filterActive = true
                    default:
                        break
                    }
                    
                }
            } else {
                filteredList = hospitals
                filterActive = false
            }
            if (pinnedList.count > 0) {
                handlePins()
            }
            //print(filteredList[0].name)
            tableView.reloadData()
        }
}
