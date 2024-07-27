import class Foundation.Bundle

extension Foundation.Bundle {
    static let module: Bundle = {
        let mainPath = Bundle.main.bundleURL.appendingPathComponent("sudoku-skip_Sudoku.bundle").path
        let buildPath = "/Users/usman_nazir/Desktop/sudoku-skip/.build/arm64-apple-macosx/debug/sudoku-skip_Sudoku.bundle"

        let preferredBundle = Bundle(path: mainPath)

        guard let bundle = preferredBundle ?? Bundle(path: buildPath) else {
            fatalError("could not load resource bundle: from \(mainPath) or \(buildPath)")
        }

        return bundle
    }()
}