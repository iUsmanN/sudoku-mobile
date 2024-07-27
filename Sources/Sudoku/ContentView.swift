import SwiftUI

//@Observable
//struct GridData {
//    
//}

public struct ContentView: View {
    @State var appearance = ""
    
    @State var row1: [Int] = Array(repeating: 1, count: 9)
    @State var row2: [Int] = Array(repeating: 2, count: 9)
    @State var row3: [Int] = Array(repeating: 3, count: 9)
    
    @State var row4: [Int] = Array(repeating: 4, count: 9)
    @State var row5: [Int] = Array(repeating: 5, count: 9)
    @State var row6: [Int] = Array(repeating: 6, count: 9)
    
    @State var row7: [Int] = Array(repeating: 7, count: 9)
    @State var row8: [Int] = Array(repeating: 8, count: 9)
    @State var row9: [Int] = Array(repeating: 9, count: 9)
    
    @State var gridData: [Binding<[Int]>] = .init()// = [$row1, $row2, $row3]

    public init() {
        self._gridData = State(initialValue: [Binding(projectedValue: $row1),
                                              Binding(projectedValue: $row2),
                                              Binding(projectedValue: $row3),
                                              Binding(projectedValue: $row4),
                                              Binding(projectedValue: $row5),
                                              Binding(projectedValue: $row6),
                                              Binding(projectedValue: $row7),
                                              Binding(projectedValue: $row8),
                                              Binding(projectedValue: $row9)])
    }

    public var body: some View {
        GeometryReader { geometry in
            ZStack {
                Color.white
                    .ignoresSafeArea()
                VStack(alignment: .center) {
                    HeaderView()
                    GridView(data: $gridData)
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
