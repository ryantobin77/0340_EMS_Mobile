//
//  Utils.swift
//  EMS iOS App
//
//  Created by Ryan Tobin on 11/9/20.
//  Copyright © 2020 JD_0340_EMS. All rights reserved.
//

import Foundation

enum NedocsScore: String {
    case normal = "Normal"
    case busy = "Busy"
    case overcrowded = "Overcrowded"
    case severe = "Severe"
}

enum HospitalType: String {
    case adultTraumaCenterLevelI = "Adult Trauma Centers-Level I"
    case adultTraumaCenterLevelII = "Adult Trauma Centers-Level II"
    case adultTraumaCenterLevelIII = "Adult Trauma Centers-Level III"
    case adultTraumaCenterLevelIV = "Adult Trauma Center - Level 3"
    case pediatricTraumaCenterLevelI = "Pediatric Trauma Centers-Pediatric Level I"
    case pediatricTraumaCenterLevelII = "Pediatric Trauma Centers-Pediatric Level II"
    case comprehensiveStrokeCenter = "Stroke Centers-Comprehensive Stroke Center"
    case thrombectomyStrokeCenter = "Stroke Centers-Thrombectomy Capable Stroke Center"
    case primaryStrokeCenter = "Stroke Centers-Primary Stroke Center"
    case remoteStrokeCenter = "Stroke Centers-Remote Treatment Stroke Center"
    case emergencyCardiacCenterLevelI = "Emergency Cardiac Care Center-Level I ECCC"
    case emergencyCardiacCenterLevelII = "Emergency Cardiac Care Center-Level II ECCC"
    case emergencyCardiacCenterLevelIII = "Emergency Cardiac Care Center-Level III ECCC"
    case neonatalCenterLevelI = "Neonatal Center Designation-Level I Neonatal Center"
    case neonatalCenterLevelII = "Neonatal Center Designation-Level II Neonatal Center"
    case neonatalCenterLevelIII = "Neonatal Center Designation-Level III Neonatal Center"
    case maternalCenterLevelI = "Maternal Center Designation-Level I Maternal Center"
    case maternalCenterLevelII = "Maternal Center Designation-Level II Maternal Center"
    case maternalCenterLevelIII = "Maternal Center Designation-Level III Maternal Center"
}

enum SortType: String {
    case distance = "Distance"
    case az = "Alphabetical"
    case nedocs = "NEDOCS Score"
}
