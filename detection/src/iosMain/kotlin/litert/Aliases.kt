package litert

import cocoapods.TFLTensorFlowLite.TFLInterpreter
import cocoapods.TFLTensorFlowLite.TFLTensor
import kotlinx.cinterop.ExperimentalForeignApi

@OptIn(ExperimentalForeignApi::class)
internal typealias PlatformInterpreter = TFLInterpreter
@OptIn(ExperimentalForeignApi::class)
internal typealias PlatformTensor = TFLTensor