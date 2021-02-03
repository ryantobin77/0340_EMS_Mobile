//
//  HomeVC.swift
//  EMS iOS App
//
//  Created by Ryan Tobin on 11/9/20.
//  Copyright © 2020 JD_0340_EMS. All rights reserved.
//

import UIKit

class HomeVC: UIViewController, UITableViewDelegate, UITableViewDataSource {
    
    @IBOutlet var tableView: UITableView!
    var hospitals: Array<HospitalIH>!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        self.navigationController?.navigationBar.barStyle = UIBarStyle.black
        self.tableView.delegate = self
        self.tableView.dataSource = self
        self.getHospitals()
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
            cell.medicalDiversionIcon.image = UIImage(named: "WarningIcon")
        } else {
            cell.medicalDiversionIcon.image = nil
        }
        cell.hospitalTypeIcon.image = hospital.hospitalType.getHospitalIcon()
        cell.distanceLabel.text = "\(String(hospital.distance)) mi"
        return cell
    }
    
    func getHospitals() {
        hospitals = Array<HospitalIH>()
        hospitals.append(HospitalIH(name: "Grady Health System", nedocsScore: .severe, hospitalType: .adultTraumaCenter, distance: 0.5, hasDiversion: true))
        hospitals.append(HospitalIH(name: "WellStar - South", nedocsScore: .normal, hospitalType: .heart, distance: 1.1, hasDiversion: false))
        hospitals.append(HospitalIH(name: "Emory - University Hospital", nedocsScore: .busy, hospitalType: .brain, distance: 1.3, hasDiversion: true))
        hospitals.append(HospitalIH(name: "Hospital 4", nedocsScore: .busy, hospitalType: .brain, distance: 1.5, hasDiversion: false))
        hospitals.append(HospitalIH(name: "Hospital 5", nedocsScore: .normal, hospitalType: .heart, distance: 1.6, hasDiversion: false))
        hospitals.append(HospitalIH(name: "Hospital 6", nedocsScore: .normal, hospitalType: .heart, distance: 2.0, hasDiversion: true))
        hospitals.append(HospitalIH(name: "Hospital 7", nedocsScore: .severe, hospitalType: .brain, distance: 3.5, hasDiversion: true))
        hospitals.append(HospitalIH(name: "Hospital 8", nedocsScore: .busy, hospitalType: .brain, distance: 5.7, hasDiversion: false))
        hospitals.append(HospitalIH(name: "Hospital 9", nedocsScore: .severe, hospitalType: .brain, distance: 7.9, hasDiversion: false))
        hospitals.append(HospitalIH(name: "Hospital 10", nedocsScore: .severe, hospitalType: .adultTraumaCenter, distance: 10.5, hasDiversion: true))
    }
    
}
