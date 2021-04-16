//
//  Utils.swift
//  EMS iOS App
//
//  Created by Ryan Tobin on 11/9/20.
//  Copyright © 2020 JD_0340_EMS. All rights reserved.
//

import Foundation

enum NedocsScore: String, Codable, Comparable {
    case normal = "Normal"
    case busy = "Busy"
    case overcrowded = "Overcrowded"
    case severe = "Severe"
    
    private var comparisonValue: Int {
            switch self {
            case .normal:
                return 0
            case .busy:
                return 1
            case .overcrowded:
                return 2
            case .severe:
                return 3
            }
        }

        static func < (lhs: Self, rhs: Self) -> Bool {
            return lhs.comparisonValue < rhs.comparisonValue
        }
}
enum HospitalType: String, Codable, CaseIterable {
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

enum FilterType: String {
    case county = "County"
    case emsRegion = "EMS Region"
    case rch = "Regional Coordinating Hospital"
    case type = "Type"
}

enum SortType: String {
    case distance = "Distance"
    case az = "Alphabetical"
    case nedocs = "NEDOCS Score"
}

struct Hospital: Codable {
    var name: String!
    var nedocsScore: NedocsScore!
    var specialtyCenters: [HospitalType]!
    var distance: Double!
    var lat: Double?
    var long: Double?
    var hasDiversion: Bool!
    var diversions: [String]!
    var address: String!
    var phoneNumber: String!
    var regionNumber: String!
    var county: String!
    var rch: String! //Regional Coordinating Hospital
}
