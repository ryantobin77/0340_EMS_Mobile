//
//  HospitalIH.swift
//  EMS iOS App
//
//  Created by Ryan Tobin on 11/9/20.
//  Copyright © 2020 JD_0340_EMS. All rights reserved.
//

import UIKit

class HospitalIH: NSObject {
    
    var name: String!
    var nedocsScore: NedocsScore!
    var hospitalType: HospitalType!
    var distance: Float!
    var hasDiversion: Bool!
    
    init(name: String, nedocsScore: NedocsScore, hospitalType: HospitalType, distance: Float, hasDiversion: Bool) {
        self.name = name
        self.nedocsScore = nedocsScore
        self.hospitalType = hospitalType
        self.distance = distance
        self.hasDiversion = hasDiversion
    }
    
}
