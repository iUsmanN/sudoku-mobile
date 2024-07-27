//
//  SwiftUIView.swift
//  
//
//  Created by Usman Nazir on 27/07/2024.
//

import SwiftUI

struct GridView: View {
    
    var body: some View {
        VStack(spacing: 0) {
            GridRow()
            GridRow()
            GridRow()
            GridRow()
            GridRow()
            GridRow()
            GridRow()
            GridRow()
            GridRow()
        }
    }
    
//    var body: some View {
//        ZStack {
//            Color.green
//            Grid(horizontalSpacing: 0, verticalSpacing: 0) {
//                GridRow {
//                    GridBox()
//                    GridBox()
//                    GridBox()
//                    GridBox()
//                    GridBox()
//                    GridBox()
//                    GridBox()
//                    GridBox()
//                    GridBox()
//                }
//                GridRow {
//                    GridBox()
//                    GridBox()
//                    GridBox()
//                    GridBox()
//                    GridBox()
//                    GridBox()
//                    GridBox()
//                    GridBox()
//                    GridBox()
//                }
//                GridRow {
//                    GridBox()
//                    GridBox()
//                    GridBox()
//                    GridBox()
//                    GridBox()
//                    GridBox()
//                    GridBox()
//                    GridBox()
//                    GridBox()
//                }
//                GridRow {
//                    GridBox()
//                    GridBox()
//                    GridBox()
//                    GridBox()
//                    GridBox()
//                    GridBox()
//                    GridBox()
//                    GridBox()
//                    GridBox()
//                }
//                GridRow {
//                    GridBox()
//                    GridBox()
//                    GridBox()
//                    GridBox()
//                    GridBox()
//                    GridBox()
//                    GridBox()
//                    GridBox()
//                    GridBox()
//                }
//                GridRow {
//                    GridBox()
//                    GridBox()
//                    GridBox()
//                    GridBox()
//                    GridBox()
//                    GridBox()
//                    GridBox()
//                    GridBox()
//                    GridBox()
//                }
//                GridRow {
//                    GridBox()
//                    GridBox()
//                    GridBox()
//                    GridBox()
//                    GridBox()
//                    GridBox()
//                    GridBox()
//                    GridBox()
//                    GridBox()
//                }
//                GridRow {
//                    GridBox()
//                    GridBox()
//                    GridBox()
//                    GridBox()
//                    GridBox()
//                    GridBox()
//                    GridBox()
//                    GridBox()
//                    GridBox()
//                }
//                GridRow {
//                    GridBox()
//                    GridBox()
//                    GridBox()
//                    GridBox()
//                    GridBox()
//                    GridBox()
//                    GridBox()
//                    GridBox()
//                    GridBox()
//                }
//            }
//        }
//    }
}

struct GridBox: View {
    var body: some View {
        Text("  1  ")
            .frame(width: 40, height: 40)
            .border(Color.black)
    }
}

struct GridRow: View {
    var body: some View {
        HStack(spacing: 0) {
            GridBox()
            GridBox()
            GridBox()
            GridBox()
            GridBox()
            GridBox()
            GridBox()
            GridBox()
            GridBox()
        }
    }
}

#Preview {
    GridView()
}
