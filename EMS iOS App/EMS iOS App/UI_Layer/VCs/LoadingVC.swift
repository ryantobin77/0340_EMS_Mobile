//
//  LoadingVC.swift
//  EMS iOS App
//
//  Created by Ryan Tobin on 3/11/21.
//  Copyright © 2021 JD_0340_EMS. All rights reserved.
//

import UIKit
import MapKit

class LoadingVC: UIViewController {
    
    @IBOutlet var spinner: UIActivityIndicatorView!
    var hospitals: Array<HospitalIH>!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        self.spinner.startAnimating()
        let tasker = HospitalsTasker()
        tasker.locationManager.requestWhenInUseAuthorization()
        self.hospitals = Array<HospitalIH>()
        tasker.getAllHospitals(failure: {
            print("Failure")
        }, success: { (hospitals) in
            guard let hospitals = hospitals else {
                self.hospitals = Array<HospitalIH>()
                DispatchQueue.main.async {
                    self.spinner.stopAnimating()
                    self.performSegue(withIdentifier: "showHospitals", sender: self)
                }
                return
            }
            self.hospitals = hospitals
            tasker.getHospitalDistances(hospitals: self.hospitals, finished: { (hospitals) in
                self.hospitals = hospitals
                self.spinner.stopAnimating()
                self.performSegue(withIdentifier: "showHospitals", sender: self)
            })
        })
    }
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        let navVC = segue.destination as! UINavigationController
        let homeVC = navVC.viewControllers.first as! HomeVC
        homeVC.hospitals = self.hospitals
    }
    

}
