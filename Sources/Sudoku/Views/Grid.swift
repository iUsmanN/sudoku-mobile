//
//  SwiftUIView.swift
//  
//
//  Created by Usman Nazir on 27/07/2024.
//

import SwiftUI

struct GridView: View {
    
    @Binding var data: [Binding<[Int]>]
    
    var body: some View {
        VStack(spacing: 0) {
            GridRow(data: data[0])
            GridRow(data: data[1])
            GridRow(data: data[2])
            GridRow(data: data[3])
            GridRow(data: data[4])
            GridRow(data: data[5])
            GridRow(data: data[6])
            GridRow(data: data[7])
            GridRow(data: data[8])
        }
        .background {
            VStack(spacing: 0) {
                HStack(spacing: 0) {
                    Color.blue.opacity(0.05)
                    Color.blue.opacity(0.15)
                    Color.blue.opacity(0.05)
                }
                HStack(spacing: 0) {
                    Color.blue.opacity(0.15)
                    Color.blue.opacity(0.05)
                    Color.blue.opacity(0.15)
                }
                HStack(spacing: 0) {
                    Color.blue.opacity(0.05)
                    Color.blue.opacity(0.15)
                    Color.blue.opacity(0.05)
                }
            }
        }
    }
}

struct GridBox: View {
    
    var value: Int
    
    var body: some View {
        Button(action: {
            print("select")
        }, label: {
            Text("\(value)")
                .frame(width: 40, height: 40)
                .border(Color.black)
        })
    }
}

struct GridRow: View {
    
    @Binding var data: [Int]
    
    var body: some View {
        HStack(spacing: 0) {
            GridBox(value: data[0])
            GridBox(value: data[1])
            GridBox(value: data[2])
            GridBox(value: data[3])
            GridBox(value: data[4])
            GridBox(value: data[5])
            GridBox(value: data[6])
            GridBox(value: data[7])
            GridBox(value: data[8])
//            GridBox()
//            GridBox()
//            GridBox()
//            GridBox()
//            GridBox()
//            GridBox()
//            GridBox()
//            GridBox()
        }
    }
}

#Preview {
    @State var row1: [Int] = Array(repeating: 1, count: 9)
    @State var row2: [Int] = Array(repeating: 2, count: 9)
    @State var row3: [Int] = Array(repeating: 3, count: 9)
    
    @State var row4: [Int] = Array(repeating: 4, count: 9)
    @State var row5: [Int] = Array(repeating: 5, count: 9)
    @State var row6: [Int] = Array(repeating: 6, count: 9)
    
    @State var row7: [Int] = Array(repeating: 7, count: 9)
    @State var row8: [Int] = Array(repeating: 8, count: 9)
    @State var row9: [Int] = Array(repeating: 9, count: 9)
    
    @State var gridData: [Binding<[Int]>] = [$row1, 
                                             $row2,
                                             $row3,
                                             $row4,
                                             $row5,
                                             $row6,
                                             $row7,
                                             $row8,
                                             $row9
    ]
    
    return GridView(data: $gridData)
}
