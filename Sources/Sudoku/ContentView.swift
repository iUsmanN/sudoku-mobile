import SwiftUI

class GridData: ObservableObject {
    
    static var shared: GridData = GridData()
    
//    @Published var row1: [Int] = Array(repeating: 1, count: 9)
//    @Published var row2: [Int] = Array(repeating: 2, count: 9)
//    @Published var row3: [Int] = Array(repeating: 3, count: 9)
//    
//    @Published var row4: [Int] = Array(repeating: 4, count: 9)
//    @Published var row5: [Int] = Array(repeating: 5, count: 9)
//    @Published var row6: [Int] = Array(repeating: 6, count: 9)
//    
//    @Published var row7: [Int] = Array(repeating: 7, count: 9)
//    @Published var row8: [Int] = Array(repeating: 8, count: 9)
//    @Published var row9: [Int] = Array(repeating: 9, count: 9)
    
    @Published var grid: [[Int]] = [
        Array(repeating: 1, count: 9),
        Array(repeating: 2, count: 9),
        Array(repeating: 3, count: 9),
        Array(repeating: 4, count: 9),
        Array(repeating: 5, count: 9),
        Array(repeating: 6, count: 9),
        Array(repeating: 7, count: 9),
        Array(repeating: 8, count: 9),
        Array(repeating: 9, count: 9)
    ]
    
    var highlightedRow: Int? = 0//nil
    var highlightedIndex: Int? = 0//nil
    
    func updateValue(value: Int) {
        guard let highlightedRow, let highlightedIndex else { return }
        grid[highlightedRow][highlightedIndex] = value
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
                    GridView()
                        .frame(width: geometry.size.width, height: geometry.size.width)
                    InputView()
                        .frame(height: geometry.size.height*0.35)
                }
                .border(.red)
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
