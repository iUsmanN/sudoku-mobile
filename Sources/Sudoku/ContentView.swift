import SwiftUI

class GridData: ObservableObject {
    
    static var shared: GridData = GridData(grid: Array(repeating: Array(repeating: 0, count: 9), count: 9))

    @Published var grid: [[Int]] = Array(repeating: Array(repeating: 0, count: 9), count: 9)
    @Published var originalGrid: [[Int]] = Array(repeating: Array(repeating: 0, count: 9), count: 9)
    @Published var solutionGrid: [[Int]] = Array(repeating: Array(repeating: 0, count: 9), count: 9)
    @Published var board: SudokuBoard?
    
    @Published var highlightedRow: Int? = nil
    @Published var highlightedIndex: Int? = nil
    
    @Published var showSolution: Bool = false
    
    func updateValue(value: Int) {
        guard let highlightedRow, let highlightedIndex else { return }
        withAnimation(.easeInOut(duration: 0.25)) {
            grid[highlightedRow][highlightedIndex] = value
        }
        
    }
    
    init(grid: [[Int]], highlightedRow: Int? = nil, highlightedIndex: Int? = nil) {
        self.grid = grid
        self.highlightedRow = highlightedRow
        self.highlightedIndex = highlightedIndex

        refresh()
    }
    
    func refresh() {
        fetchSudokuBoard { board in
            DispatchQueue.main.async {
                withAnimation {
                    self.grid = Array(repeating: Array(repeating: 0, count: 9), count: 9)
                }
            }
            guard let convertedGrid = board?.toGrid, let convertedSolution = board?.solutionGrid else { return }
            DispatchQueue.main.async {
                withAnimation {
                    self.grid = convertedGrid
                    self.originalGrid = convertedGrid
                    self.solutionGrid = convertedSolution
                }
            }
        }
    }
    
    func restart() {
        DispatchQueue.main.async {
            withAnimation {
                self.grid = self.originalGrid
            }
        }
    }
}

public struct ContentView: View {
    @State var appearance = ""
    @ObservedObject var data = GridData.shared

    public var body: some View {
        GeometryReader { geometry in
            ZStack {
                Color.white
                    .ignoresSafeArea()
                VStack(alignment: .center) {
                    HeaderView()
                    ZStack {
                        GridView()
                        SolutionView()
                            .opacity(data.showSolution ? 1.0 : 0.0)
                            .scaleEffect(data.showSolution ? 1.0 : 1.05)
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
