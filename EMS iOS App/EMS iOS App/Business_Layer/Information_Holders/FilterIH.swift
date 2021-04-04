//
//  FilterIH.swift
//  EMS iOS App
//
//  Created by Allison Nakazawa on 3/28/21.
//  Copyright © 2021 JD_0340_EMS. All rights reserved.
//
import UIKit

class FilterIH: NSObject {
    var field: FilterType!
    var value: String!
    
    init(field: FilterType, value: String) {
        self.field = field
        self.value = value
    }
}
