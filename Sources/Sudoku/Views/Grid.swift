//
//  SwiftUIView.swift
//  
//
//  Created by Usman Nazir on 27/07/2024.
//

import SwiftUI

struct GridView: View {

    @ObservedObject var gridData = GridData.shared
    
    var body: some View {
        VStack(spacing: 0) {
            GridRow(row: 0)
            GridRow(row: 1)
            GridRow(row: 2)
            GridRow(row: 3)
            GridRow(row: 4)
            GridRow(row: 5)
            GridRow(row: 6)
            GridRow(row: 7)
            GridRow(row: 8)
        }
        .background {
            VStack(spacing: 0) {
                HStack(spacing: 0) {
                    Color.gray.opacity(0.05)
                    Color.gray.opacity(0.2)
                    Color.gray.opacity(0.05)
                }
                HStack(spacing: 0) {
                    Color.gray.opacity(0.2)
                    Color.gray.opacity(0.05)
                    Color.gray.opacity(0.2)
                }
                HStack(spacing: 0) {
                    Color.gray.opacity(0.05)
                    Color.gray.opacity(0.2)
                    Color.gray.opacity(0.05)
                }
            }
        }
    }
}

struct GridBox: View {
    
    var row: Int
    var col: Int
    
    @ObservedObject var gridData = GridData.shared
    
    var body: some View {
        Button(action: {
            gridData.highlightedRow = row
            gridData.highlightedIndex = col
        }, label: {
            Text("\(gridData.grid[row][col])")
                .frame(width: 40, height: 40)
                .border(Color.gray)
                .background {
                    if gridData.highlightedRow == row && gridData.highlightedIndex == col {
                        Color.orange
                    } 
//                    else if gridData.highlightedRow == row {
//                        Color.orange.opacity(0.15)
//                    } else if gridData.highlightedIndex == col {
//                        Color.orange.opacity(0.15)
//                    }
                }
                .foregroundStyle(.black)
        })
    }
}

struct GridRow: View {
    var row: Int
    
    var body: some View {
        HStack(spacing: 0) {
            GridBox(row: row, col: 0)
            GridBox(row: row, col: 1)
            GridBox(row: row, col: 2)
            GridBox(row: row, col: 3)
            GridBox(row: row, col: 4)
            GridBox(row: row, col: 5)
            GridBox(row: row, col: 6)
            GridBox(row: row, col: 7)
            GridBox(row: row, col: 8)
        }
    }
}

#Preview {
    GridView()
}
