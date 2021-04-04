//
//  FilterButton.swift
//  EMS iOS App
//
//  Created by Allison Nakazawa on 4/2/21.
//  Copyright © 2021 JD_0340_EMS. All rights reserved.
//
import UIKit

class FilterButton: UIButton {
    var field: FilterType!
    var value: String!

    convenience init(field: FilterType, value: String) {
        self.init()
        self.field = field
        self.value = value
    }
}
