//
//  SwiftUIView.swift
//  
//
//  Created by Usman Nazir on 27/07/2024.
//

import SwiftUI

struct HeaderView: View {
    var body: some View {
        VStack {
            Text("00:00:00").font(.title2)
            Text("MEDIUM".uppercased()).font(.title).bold()
        }
    }
}

#Preview {
    HeaderView()
}
