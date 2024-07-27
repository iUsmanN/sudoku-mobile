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
    }
}

struct SolutionView: View {

    @ObservedObject var gridData = GridData.shared
    
    var body: some View {
        VStack(spacing: 0) {
            SolutionRow(row: 0)
            SolutionRow(row: 1)
            SolutionRow(row: 2)
            SolutionRow(row: 3)
            SolutionRow(row: 4)
            SolutionRow(row: 5)
            SolutionRow(row: 6)
            SolutionRow(row: 7)
            SolutionRow(row: 8)
        }
    }
}

struct SolutionBox: View {
    
    var row: Int
    var col: Int
    var disabled: Bool = false
    
    @ObservedObject var gridData = GridData.shared
    
    init(row: Int, col: Int) {
        self.row = row
        self.col = col
    }
    
    var body: some View {
        Button(action: {
            gridData.highlightedRow = row
            gridData.highlightedIndex = col
        }, label: {
            Text("\(gridData.solutionGrid[row][col] == 0 ? " " : "\(gridData.solutionGrid[row][col])")")
                .frame(width: 40, height: 40)
                .border(Color.black.opacity(0.15))
                .background {
                    ZStack {
                        if !gridData.originalGrid.isEmpty && !gridData.originalGrid[0].isEmpty && gridData.originalGrid[row][col] != 0 {
                            Color.gray.opacity(0.4)
                        } else {
                            Color.white
                        }
                    }
                }
                .foregroundStyle(.black)
        })
        .disabled(!gridData.originalGrid.isEmpty && !gridData.originalGrid[0].isEmpty && gridData.originalGrid[row][col] != 0)
    }
}

struct GridBox: View {
    
    var row: Int
    var col: Int
    var disabled: Bool = false
    
    @ObservedObject var gridData = GridData.shared
    
    init(row: Int, col: Int) {
        self.row = row
        self.col = col
        print("\(row), \(col), \(gridData.grid[row][col])")
    }
    
    var body: some View {
        Button(action: {
            gridData.highlightedRow = row
            gridData.highlightedIndex = col
        }, label: {
            Text("\(gridData.grid[row][col] == 0 ? " " : "\(gridData.grid[row][col])")")
                .frame(width: 40, height: 40)
                .border(Color.black.opacity(0.15))
            #if !SKIP
                .contentTransition(.numericText(countsDown: true))
            #endif
                .background {
                    ZStack {
                        if !gridData.originalGrid.isEmpty && !gridData.originalGrid[0].isEmpty && gridData.originalGrid[row][col] != 0 {
                            Color.gray.opacity(0.4)
                        }
                        if gridData.highlightedRow == row && gridData.highlightedIndex == col {
                            Color.yellow
                        }
                        else if gridData.highlightedRow == row {
                            Color.yellow.opacity(0.15)
                        } else if gridData.highlightedIndex == col {
                            Color.yellow.opacity(0.15)
                        }
                    }
                }
                .foregroundStyle(.black)
        })
        .disabled(!gridData.originalGrid.isEmpty && !gridData.originalGrid[0].isEmpty && gridData.originalGrid[row][col] != 0)
    }
}

struct SolutionRow: View {
    var row: Int
    
    var body: some View {
        HStack(spacing: 0) {
            SolutionBox(row: row, col: 0)
            SolutionBox(row: row, col: 1)
            SolutionBox(row: row, col: 2)
            SolutionBox(row: row, col: 3)
            SolutionBox(row: row, col: 4)
            SolutionBox(row: row, col: 5)
            SolutionBox(row: row, col: 6)
            SolutionBox(row: row, col: 7)
            SolutionBox(row: row, col: 8)
        }
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
