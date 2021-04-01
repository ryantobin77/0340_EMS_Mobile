//
//  SortSelectorView.swift
//  EMS iOS App
//
//  Created by Anna Dingler on 4/1/21.
//  Copyright © 2021 JD_0340_EMS. All rights reserved.
//

import UIKit

protocol SortSelectorDelegate: class {
    func onSortSelected(_ sort: SortType?)
}

class SortSelectorView: UIView {
    
    @IBOutlet weak var contentView: UIView!
    @IBOutlet weak var nameSort: UIButton!
    @IBOutlet weak var distanceSort: UIButton!
    @IBOutlet weak var nedocsSort: UIButton!
        
    weak var delegate: SortSelectorDelegate?
    
    var currentSort: SortType?
    
    override init(frame: CGRect) {
        super.init(frame: frame)
        commonInit()
    }
    
    required init?(coder aDecoder: NSCoder) {
        super.init(coder: aDecoder)
        commonInit()
    }
    
    func commonInit() {
        Bundle.main.loadNibNamed("SortSelectorView", owner: self, options: nil)
        contentView.fixInView(self)
    }
    
    @IBAction func onSortClickListener(_ sender: UIButton) {
        
        switch sender {
        case nameSort:
            // Update background colors
            sender.backgroundColor = UIColor(named: "MainColorHighlight")
            distanceSort?.backgroundColor = UIColor.systemBackground
            nedocsSort?.backgroundColor = UIColor.systemBackground
            
            delegate?.onSortSelected(SortType.az)
        case distanceSort:
            // Update background colors
            sender.backgroundColor = UIColor(named: "MainColorHighlight")
            nameSort?.backgroundColor = UIColor.systemBackground
            nedocsSort?.backgroundColor = UIColor.systemBackground
            
            delegate?.onSortSelected(SortType.distance)
        case nedocsSort:
            // Update background colors
            sender.backgroundColor = UIColor(named: "MainColorHighlight")
            nameSort?.backgroundColor = UIColor.systemBackground
            distanceSort?.backgroundColor = UIColor.systemBackground
            
            delegate?.onSortSelected(SortType.nedocs)
        default:
            print("Unknown sort button selected")
        }
    }
}

extension UIView
{
    func fixInView(_ container: UIView!) -> Void{
        self.translatesAutoresizingMaskIntoConstraints = false;
        self.frame = container.frame;
        container.addSubview(self);
        NSLayoutConstraint(item: self, attribute: .leading, relatedBy: .equal, toItem: container, attribute: .leading, multiplier: 1.0, constant: 0).isActive = true
        NSLayoutConstraint(item: self, attribute: .trailing, relatedBy: .equal, toItem: container, attribute: .trailing, multiplier: 1.0, constant: 0).isActive = true
        NSLayoutConstraint(item: self, attribute: .top, relatedBy: .equal, toItem: container, attribute: .top, multiplier: 1.0, constant: 0).isActive = true
        NSLayoutConstraint(item: self, attribute: .bottom, relatedBy: .equal, toItem: container, attribute: .bottom, multiplier: 1.0, constant: 0).isActive = true
    }
}
