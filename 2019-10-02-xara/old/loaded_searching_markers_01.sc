# initial record
Version(2)
Include(scripts/common/initiating.sc)
Include(scripts/common/debugging.sc)

DeployFolders:
    GlobPattern(_FUZZING_SAMPLES/xara/originals_01/*.xar)
    HostDeployInputGlob

    Return

Main:
    RegisterSignals(Exception)

    Call(Intro)
    Call(DeployFolders)

    QemuStart
    QemuLoad(loaded)


    GetPIDByMatch(WebDesigner)
    Push
    SetPID

    Call(AttachFileLog)

    Main_Copy:
        RunCommand(copy \\10.0.2.4\qemu\input\origin01.xar c:\users\john\desktop\sample.xar)
        CheckStrStr(cannot)=(Y:Main_Sleep)
        CheckStrStr(network)=(Y:Main_Sleep)

    RegisterReactions(kernel32.dll+0x000528fc,CreateFileA_start,0x0)
    RegisterReactions(kernel32.dll+0x00050b5d,CreateFileW_start,0x0)
    EnableReaction(CreateFileA_start)
    EnableReaction(CreateFileW_start)

    RunCommand(start c:\users\john\desktop\sample.xar)
    Continue=

    Main_Sleep:
        Wait(1)
        goto(Main_Copy)

CreateFileA_start:
    ReadArgAnsi(0x1)
    CheckStrStr(sample)=(N:CreateFileA_start_skip)
    CompareCounter(1)=(Y:CreateFileA_start_skip)
    Call(StartAnalysis)
    IncreaseCounter
    

    CreateFileA_start_skip:

    Continue=

CreateFileW_start:
    ReadArgUni(0x1)
    CheckStrStr(sample)=(N:CreateFileW_start_skip)
    CompareCounter(1)=(Y:CreateFileW_start_skip)
    Call(StartAnalysis)
    IncreaseCounter
    
    CreateFileW_start_skip:

    Continue=
    
StartAnalysis:
    DumpMemory
    #SecureAllSections
    #AddScannedLocation(ESP:0x50)

#    Call(RegisterEnableBuiltin)

    #ResizeOutBuffer(0x10000000)
    #ResizeModBuffer(0x1000000)
    #ChangeInterval(10000000)

    #Pause
    #TracerStartTrace
    TracerStartTraceLight
    #TracerStartTraceDebug
    TracerDebugContinueInf
    Return

RE:
    TracerGetExceptionCode
    CheckStrStr(0xe06d7363)=(Y:RE_continue)
    CheckStrStr(0x406d1388)=(Y:RE_continue)
    CheckStrStr(0xe0434352)=(Y:RE_continue)

RE_crash:
    FlushFiles
    Pause

RE_continue:
    Continue(0x80010001)=

Default:
    Continue=

Exception:
finish:
    QemuQuit

