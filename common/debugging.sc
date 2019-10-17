ReconfigureDirs:
    TracerConfigureOutDir
    TracerConfigureOutPrefix
    TracerConfigureInDir
    TracerPrepareTrace
    HandleExceptions
    Return

Debug:
    SpawnTracerNoLog
    TracerConfigureSample
    TracerConfigureOutDir
    TracerConfigureOutPrefix
    TracerConfigureInDir
    TracerPrepareTrace
    HandleExceptions
    TracerDebugSample
    Return

DebugScrLog:
    SpawnTracerScrLog
    TracerConfigureSample
    TracerConfigureOutDir
    TracerConfigureOutPrefix
    TracerConfigureInDir
    TracerPrepareTrace
    HandleExceptions
    TracerDebugSample
    Return

DebugFileLog:
    SpawnTracerFileLog
    TracerConfigureSample
    TracerConfigureOutDir
    TracerConfigureOutPrefix
    TracerConfigureInDir
    TracerPrepareTrace
    HandleExceptions
    TracerDebugSample
    Return

DebugRemoteLog:
    SpawnTracerFileLog
    TracerConfigureSample
    TracerConfigureOutDir
    TracerConfigureOutPrefix
    TracerConfigureInDir
    TracerPrepareTrace
    HandleExceptions
    TracerDebugSample
    Return

EnableThreadTracking:
    EnableReaction(CreateProcessA_start)
    EnableReaction(CreateProcessW_start)
    EnableReaction(CreateRemoteThread_start)
    EnableReaction(SetThreadContext_start)
    EnableReaction(ResumeThread_start)
    # OpenProcess
    # 
    Return

DisableThreadTracking:
    DisableReaction(CreateProcessA_start)
    DisableReaction(CreateProcessW_start)
    DisableReaction(CREATETHREAD+)
    DisableReaction(CREATEREMOTETHREAD+)
    DisableReaction(OPENPROCESS+)
    DisableReaction(SETTHREADCONTEXT+)
    DisableReaction(RESUMETHREAD+)
    Return

PauseNewProcess:
    ReadRegister(ESP)

    Adjust(0x18)
    Push
    WriteDword(0x4)
    Return

ExtractPIDAndTID:
    ReadRegister(ESP)
    #Adjust(0x2c)
    Adjust(0x28)
    Push
    ReadDword
    Adjust(0x8)
    Push
    ReadPID

    ReadRegister(ESP)
    #Adjust(0x2c)
    Adjust(0x28)
    Push
    ReadDword
    Adjust(0xc)
    Push
    ReadTID
    Return

Attach:
    SpawnTracerNoLog
    TracerConfigureSamplePID
    TracerConfigureOutDir
    TracerConfigureOutPrefix
    TracerAppendPIDPrefix
    TracerConfigureInDir
    TracerPrepareTrace
    HandleExceptions
    TracerAttachSample
    Return

AttachScrLog:
    SpawnTracerScrLog
    TracerConfigureSamplePID
    TracerConfigureOutDir
    TracerConfigureOutPrefix
    TracerAppendPIDPrefix
    TracerConfigureInDir
    TracerPrepareTrace
    HandleExceptions
    TracerAttachSample
    Return

AttachFileLog:
    SpawnTracerFileLog
    TracerConfigureSamplePID
    TracerConfigureOutDir
    TracerConfigureOutPrefix
    TracerAppendPIDPrefix
    TracerConfigureInDir
    TracerPrepareTrace
    HandleExceptions
    TracerAttachSample
    Return

AttachRemoteLog:
    SpawnTracerRemoteLog
    TracerConfigureSamplePID
    TracerConfigureOutDir
    TracerConfigureOutPrefix
    TracerAppendPIDPrefix
    TracerConfigureInDir
    TracerPrepareTrace
    HandleExceptions
    TracerAttachSample
    Return

RegisterEnableBuiltin:
    TracerRegisterBuiltin
    EnableBuiltin
    ExclusiveBuiltin
    LowerBuiltin
    EnableReaction(KiFastSystemCall_start)
    RaiseReaction(KiFastSystemCall_start)
    Return

# placed on caller
SuspendCreatedRemoteThread:
    ReadRegister(ESP)
    Adjust(0x14)
    Push
    WriteDword(0x4)
    Return

