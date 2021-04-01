//
//  SortSelectorVC.swift
//  EMS iOS App
//
//  Created by Anna Dingler on 3/31/21.
//  Copyright © 2021 JD_0340_EMS. All rights reserved.
//

import Foundation
import UIKit

protocol SortSelectorDelegate: class {
    func onSortSelected(_ sort: SortType?)
}

class SortSelectorViewController: UIViewController {
    
    @IBOutlet var nameSort: UIButton!
    @IBOutlet var distanceSort: UIButton!
    @IBOutlet var nedocsSort: UIButton!
    
    var currentSort: SortType! = nil
    weak var delegate: SortSelectorDelegate?
        
    override func viewDidLoad() {
        super.viewDidLoad()
        
        preferredContentSize = view.systemLayoutSizeFitting(UIView.layoutFittingCompressedSize)
                
        if (currentSort != nil) {
            switch currentSort! {
            case SortType.az:
                nameSort.backgroundColor = UIColor(named: "MainColorHighlight")
            case SortType.distance:
                distanceSort.backgroundColor = UIColor(named: "MainColorHighlight")
            case SortType.nedocs:
                nedocsSort.backgroundColor = UIColor(named: "MainColorHighlight")
            }
        }
        
        
    }
    
    @IBAction func onSortClickListener(_ sender: UIButton) {
        
        switch sender {
        case nameSort:
            sender.backgroundColor = UIColor(named: "MainColorHighlight")
            delegate?.onSortSelected(SortType.az)
        case distanceSort:
            sender.backgroundColor = UIColor(named: "MainColorHighlight")
            delegate?.onSortSelected(SortType.distance)
        case nedocsSort:
            sender.backgroundColor = UIColor(named: "MainColorHighlight")
            delegate?.onSortSelected(SortType.nedocs)
        default:
            print("Unknown sort button selected")
        }
        self.presentingViewController?.dismiss(animated: true, completion: nil)
    }
}
