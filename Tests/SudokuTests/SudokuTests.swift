import XCTest
import OSLog
import Foundation
@testable import Sudoku

let logger: Logger = Logger(subsystem: "Sudoku", category: "Tests")

@available(macOS 13, *)
final class SudokuTests: XCTestCase {
    func testSudoku() throws {
        logger.log("running testSudoku")
        XCTAssertEqual(1 + 2, 3, "basic test")
        
        // load the TestData.json file from the Resources folder and decode it into a struct
        let resourceURL: URL = try XCTUnwrap(Bundle.module.url(forResource: "TestData", withExtension: "json"))
        let testData = try JSONDecoder().decode(TestData.self, from: Data(contentsOf: resourceURL))
        XCTAssertEqual("Sudoku", testData.testModuleName)
    }
}

struct TestData : Codable, Hashable {
    var testModuleName: String
}