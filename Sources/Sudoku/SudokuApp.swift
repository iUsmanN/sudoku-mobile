import Foundation
import OSLog
import SwiftUI

let logger: Logger = Logger(subsystem: "game.iusmann.sudoku", category: "Sudoku")

/// The Android SDK number we are running against, or `nil` if not running on Android
let androidSDK = ProcessInfo.processInfo.environment["android.os.Build.VERSION.SDK_INT"].flatMap({ Int($0) })

/// The shared top-level view for the app, loaded from the platform-specific App delegates below.
///
/// The default implementation merely loads the `ContentView` for the app and logs a message.
public struct RootView : View {
    public init() {
    }

    public var body: some View {
        ContentView()
    }
}

#if !SKIP
public protocol SudokuApp : App {
}

/// The entry point to the Sudoku app.
/// The concrete implementation is in the SudokuApp module.
public extension SudokuApp {
    var body: some Scene {
        WindowGroup {
            RootView()
        }
    }
}
#endif
