import SwiftUI

class GridData: ObservableObject {
    
    static var shared: GridData = GridData(grid: Array(repeating: Array(repeating: 0, count: 9), count: 9))

    @Published var grid: [[Int]] = [[]]
    @Published var originalGrid: [[Int]] = [[]]
    @Published var solutionGrid: [[Int]] = [[]]
    @Published var board: SudokuBoard?
    
    @Published var highlightedRow: Int? = nil
    @Published var highlightedIndex: Int? = nil
    
    func updateValue(value: Int) {
        guard let highlightedRow, let highlightedIndex else { return }
        grid[highlightedRow][highlightedIndex] = value
    }
    
    init(grid: [[Int]], highlightedRow: Int? = nil, highlightedIndex: Int? = nil) {
        self.grid = grid
        self.highlightedRow = highlightedRow
        self.highlightedIndex = highlightedIndex
        
        fetchSudokuBoard { board in
            guard let convertedGrid = board?.toGrid, let convertedSolution = board?.solutionGrid else { return }
            DispatchQueue.main.async {
                self.grid = convertedGrid
                self.originalGrid = convertedGrid
                self.solutionGrid = convertedSolution
            }
        }
    }
    
    func refresh() {
        
    }
}

public struct ContentView: View {
    @State var appearance = ""

    public var body: some View {
        GeometryReader { geometry in
            ZStack {
                Color.white
                    .ignoresSafeArea()
                VStack(alignment: .center) {
                    HeaderView()
                    ZStack {
                        GridView()
                    }
                        .frame(width: geometry.size.width, height: geometry.size.width)
                    InputView()
                        .frame(height: geometry.size.height*0.35)
                        .padding(.top)
                }
            }
        }
        .preferredColorScheme(appearance == "dark" ? .dark : appearance == "light" ? .light : nil)
    }
}

enum Tab : String, Hashable {
    case welcome, home, settings
}

#Preview {
    ContentView()
}
