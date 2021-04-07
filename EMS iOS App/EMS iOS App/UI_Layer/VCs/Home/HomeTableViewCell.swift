//
//  HomeTableViewCell.swift
//  EMS iOS App
//
//  Created by Ryan Tobin on 11/9/20.
//  Copyright © 2020 JD_0340_EMS. All rights reserved.
//

import UIKit

class HomeTableViewCell: UITableViewCell {

    @IBOutlet var nedocsView: UIView!
    @IBOutlet var nedocsLabel: UILabel!
    @IBOutlet var hospitalName: UILabel!
    @IBOutlet var mapPinView: UIImageView!
    @IBOutlet var distanceLabel: UILabel!
    @IBOutlet var hospitalTypeIcon1: UIImageView!
    @IBOutlet var hospitalTypeIcon2: UIImageView!
    @IBOutlet var hospitalTypeIcon3: UIImageView!
    @IBOutlet var diversionIcon: UIImageView?
    @IBOutlet weak var phoneIcon: UIImageView!
    @IBOutlet var expandedDiversionIcon: UIImageView?
    @IBOutlet var expandedDiversionIconLabel: UILabel!
    @IBOutlet var expandedHospitalTypeIcon1: UIImageView!
    @IBOutlet var hospitalTypeLabel1: UILabel!
    @IBOutlet var expandedHospitalTypeIcon2: UIImageView!
    @IBOutlet var hospitalTypeLabel2: UILabel!
    @IBOutlet var expandedHospitalTypeIcon3: UIImageView!
    @IBOutlet var hospitalTypeLabel3: UILabel!
    @IBOutlet var address: UILabel!
        /*= {
        let label = UILabel()
        label.contentMode = .scaleToFill
        label.numberOfLines = 0
        //label.leadingMargin(pixel: 10)
        //label.trailingMargin(pixel: 10)
     
     return label
    }()*/
    @IBOutlet weak var minimizeButton: UIButton!
    @IBOutlet weak var expandButton: UIButton!
    @IBOutlet var phoneNumber: UILabel!
    @IBOutlet weak var countyLabel: UILabel!
    @IBOutlet weak var regionLabel: UILabel!
    @IBOutlet weak var rchLabel: UILabel!
    @IBOutlet weak var lastUpdatedLabel: UILabel!
    @IBOutlet weak var vertStackView: UIStackView!
    @IBOutlet weak var horStackView1: UIStackView!
    @IBOutlet weak var diversionsHolder: UIStackView!
    @IBOutlet weak var hospitalTypeHolder1: UIStackView!
    @IBOutlet weak var hospitalTypeHolder2: UIStackView!
    @IBOutlet weak var hospitalTypeHolder3: UIStackView!
    @IBOutlet weak var horStackView4: UIStackView!
    @IBOutlet weak var favoritePin: UIButton!
    @IBOutlet weak var horStackView5: UIStackView!
    @IBOutlet var diversionIconWidth: NSLayoutConstraint!
    @IBOutlet var diversionIconLeading: NSLayoutConstraint!
}
