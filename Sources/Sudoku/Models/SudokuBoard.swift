//
//  File.swift
//  
//
//  Created by Usman Nazir on 27/07/2024.
//

import Foundation

// Define the custom data model
struct SudokuBoard: Codable {
    struct Grid: Codable {
        let value: [[Int]]
        let solution: [[Int]]
        let difficulty: String
    }
    
    struct NewBoard: Codable {
        let grids: [Grid]
        let results: Int
        let message: String
    }
    
    let newboard: NewBoard
    
    var toGrid: [[Int]] {
        newboard.grids.first?.value ?? Array(repeating: Array(repeating: 10, count: 9), count: 9)
    }
    
    var solutionGrid: [[Int]] {
        newboard.grids.first?.solution ?? Array(repeating: Array(repeating: 10, count: 9), count: 9)
    }
}

func fetchSudokuBoard(completion: @escaping (SudokuBoard?) -> Void) {
    let urlString = "https://sudoku-api.vercel.app/api/dosuku"
    guard let url = URL(string: urlString) else {
        print("Invalid URL")
        completion(nil)
        return
    }

    let task = URLSession.shared.dataTask(with: url) { data, response, error in
        if let error = error {
            print("Error fetching data: \(error)")
            completion(nil)
            return
        }

        guard let data = data else {
            print("No data received")
            completion(nil)
            return
        }

        do {
            let decoder = JSONDecoder()
            let sudokuBoard = try decoder.decode(SudokuBoard.self, from: data)
            completion(sudokuBoard)
        } catch {
            print("Error decoding JSON: \(error)")
            completion(nil)
        }
    }

    task.resume()
}
