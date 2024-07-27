//
//  SwiftUIView.swift
//  
//
//  Created by Usman Nazir on 27/07/2024.
//

import SwiftUI

struct InputView: View {
    
    @ObservedObject var gridData = GridData.shared
    
    var body: some View {
        VStack {
            HStack {
                InputBox(value: 1)
                InputBox(value: 2)
                InputBox(value: 3)
            }
            HStack {
                InputBox(value: 4)
                InputBox(value: 5)
                InputBox(value: 6)
            }
            HStack {
                InputBox(value: 7)
                InputBox(value: 8)
                InputBox(value: 9)
            }
            HStack {
                Button(action: {
                    print("refresh")
                    gridData.restart()
                }, label: {
                    Text("Restart")
                        .font(.footnote)
                        .frame(width: 65, height: 45)
                        .border(Color.black)
                        .foregroundStyle(.black)
                })
                Button(action: {
                    print("refresh")
                    gridData.refresh()
                }, label: {
                    Text("Refresh")
                        .font(.footnote)
                        .frame(width: 65, height: 45)
                        .border(Color.black)
                        .foregroundStyle(.black)
                })
                Button(action: {
                    print("Solution")
                    withAnimation {
                        gridData.showSolution = !gridData.showSolution
                    }
                }, label: {
                    Text("Solution")
                        .font(.footnote)
                        .frame(width: 65, height: 45)
                        .border(Color.black)
                        .foregroundStyle(.black)
                })
            }
        }
    }
}

struct InputBox: View {
    
    var value: Int
    var body: some View {
        Button(action: {
            GridData.shared.updateValue(value: value)
        }, label: {
            Text("\(value)")
                .font(.title2)
                .frame(width: 65, height: 65)
                .border(Color.black)
                .bold()
                .foregroundStyle(.black)
        })
    }
}

#Preview {
    InputView()
}
