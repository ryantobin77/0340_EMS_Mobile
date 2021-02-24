//
//  HomeVC.swift
//  EMS iOS App
//
//  Created by Ryan Tobin on 11/9/20.
//  Copyright © 2020 JD_0340_EMS. All rights reserved.
//

import UIKit

class HomeVC: UIViewController, UITableViewDelegate, UITableViewDataSource, UIGestureRecognizerDelegate {
    
    @IBOutlet var tableView: UITableView!
    var hospitals: Array<HospitalIH>!
    var favoritePinTapped = false
    
    override func viewDidLoad() {
        super.viewDidLoad()
        self.navigationController?.navigationBar.barStyle = UIBarStyle.black
        self.hospitals = Array<HospitalIH>()
        self.tableView.delegate = self
        self.tableView.dataSource = self
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
            self.hospitals = hospitals
            DispatchQueue.main.async {
                self.tableView.reloadData()
            }
        })
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return hospitals.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "hospitalCell", for: indexPath) as! HomeTableViewCell
        let hospital = self.hospitals[indexPath.row]
        
        cell.hospitalName.text = hospital.name
        cell.nedocsView.layer.cornerRadius = 5.0
        cell.nedocsView.backgroundColor = hospital.nedocsScore.getNedocsColor()
        cell.nedocsLabel.text = hospital.nedocsScore.rawValue
        if (hospital.hasDiversion) {
            cell.horStackView2.isHidden = false
            cell.medicalDiversionIcon2Label2.isHidden = false
            cell.medicalDiversionIcon2Label3.isHidden = true

            cell.medicalDiversionIcon?.image = UIImage(named: "WarningIcon")
            cell.medicalDiversionIcon2?.image = UIImage(named: "WarningIcon")
            cell.medicalDiversionIcon2Label.text = "Medical Diversion"
            //cell.medicalDiversionIcon2Label2.text = "Psych Diversion"
            cell.medicalDiversionIcon2Label2.isHidden = true
        } else {
            cell.horStackView2.isHidden = true
            cell.medicalDiversionIcon?.image = nil
            cell.medicalDiversionIcon2?.image = nil
            cell.medicalDiversionIcon2Label.text = ""
            cell.medicalDiversionIcon2Label2.isHidden = true
            cell.medicalDiversionIcon2Label3.isHidden = true
        }
        cell.hospitalTypeIcon2Label.text = hospital.hospitalType.map { $0.rawValue }
        
        
        cell.hospitalTypeIcon.image = hospital.hospitalType.getHospitalIcon()
        cell.hospitalTypeIcon2.image = hospital.hospitalType.getHospitalIcon()
        cell.distanceLabel.text = "\(String(hospital.distance)) mi"
        cell.address.attributedText = NSAttributedString(string: hospital.address, attributes:
            [.underlineStyle: NSUnderlineStyle.single.rawValue])
        cell.address.textColor = UIColor.blue
        cell.phoneNumber.attributedText = NSAttributedString(string: hospital.phoneNumber, attributes:
            [.underlineStyle: NSUnderlineStyle.single.rawValue])
        cell.phoneNumber.textColor = UIColor.blue
        cell.countyLabel.text = "\(String(hospital.county)) - EMS Region \(String(hospital.regionNumber))"
        cell.rchLabel.text = "Regional Coordination Hospital \(String(hospital.rch))"
    
        return cell
    }
    
    var thereIsCellTapped = false
    var selectedRowIndex = -1

    func tableView(_ tableView: UITableView,
                   heightForRowAt indexPath: IndexPath) -> CGFloat {

        if indexPath.row == selectedRowIndex && thereIsCellTapped {
            return 200
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
        
        if self.selectedRowIndex != -1 {
            self.tableView.cellForRow(at: NSIndexPath(row: self.selectedRowIndex, section: 0) as IndexPath)?.backgroundColor = UIColor.white
        }

        if selectedRowIndex != indexPath.row {
            self.thereIsCellTapped = true
            self.selectedRowIndex = indexPath.row
            let image = UIImage(named:"ArrowIconUp")
            cell.expandButton.setImage(image, for: .normal)
        } else {
            // there is no cell selected anymore
            self.thereIsCellTapped = false
            self.selectedRowIndex = -1
            let image = UIImage(named:"ArrowIcon")
            cell.expandButton.setImage(image, for: .normal)
        }
        self.tableView.beginUpdates()
        self.tableView.endUpdates()
        
    }
    
    
    @IBAction func favoritePin(_ sender: UIButton) {
        guard let cell = sender.superview?.superview as? HomeTableViewCell else {
            return
        }
        
        if (!favoritePinTapped) {
            let myImage = UIImage(named: "FilledFavoritePin")
            cell.favoritePin.setImage(myImage, for: .normal)
            self.favoritePinTapped = true
        } else {
            let myImage = UIImage(named: "OutlineFavoritePin")
            cell.favoritePin.setImage(myImage, for: .normal)
            self.favoritePinTapped = false
        }
    }
    
}
