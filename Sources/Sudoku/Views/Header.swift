//
//  SwiftUIView.swift
//  
//
//  Created by Usman Nazir on 27/07/2024.
//

import SwiftUI

struct HeaderView: View {
    
    @ObservedObject var data = GridData.shared
    
    var body: some View {
        VStack {
            Text("00:00:00").font(.title2)
            Text(data.board?.newboard.grids.first?.difficulty.uppercased() ?? "").font(.title).bold()
        }
    }
}

#Preview {
    HeaderView()
}
