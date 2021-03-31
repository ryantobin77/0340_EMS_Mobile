//
//  FilterCard.swift
//  EMS iOS App
//
//  Created by Allison Nakazawa on 3/31/21.
//  Copyright © 2021 JD_0340_EMS. All rights reserved.
//

import UIKit

class FilterCard: UIView {
    
    var field: FilterType!
    @IBOutlet weak var valueLabel: UILabel!
    @IBOutlet weak var removeButton: UIButton!
    
    convenience init(field: FilterType) {
        self.init()
        self.field = field
    }
}
