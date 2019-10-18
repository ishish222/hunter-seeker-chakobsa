# initial record
Version(2)
Include(scripts/common/initiating.sc)
Include(scripts/common/debugging.sc)

DeployFolders:
    GlobPattern(_FUZZING_SAMPLES/xara/00_originals_03/*.xar)
    #GlobPattern(_FUZZING_SAMPLES/xara/binned_02/0x00c702de/*.xar)
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
        RunCommand(copy \\10.0.2.4\qemu\input\origin_19.xar c:\users\john\desktop\sample.xar)
        CheckStrStr(cannot)=(Y:Main_Sleep)
        CheckStrStr(network)=(Y:Main_Sleep)

    RegisterReactions(self+0xae85bf,ST,0x0)
    RegisterReactions(self+0xae8850,EN,0x0)

    EnableReaction(ST)
    EnableReaction(EN)

    RunCommand(start c:\users\john\desktop\sample.xar)

    Continue=

    Main_Sleep:
        Wait(1)
        goto(Main_Copy)

ST:
    DisableReaction(ST)
    Call(StartAnalysis)
    ResetTime
    Continue=

StartAnalysis:
    #TracerDebugLogEnable

    CurrentTID
    Push
    SetPriorityHigh

    DumpMemory
    SecureAllSections
    #AddScannedLocation(ESP:0x50)

    #Call(RegisterEnableBuiltin)

    ResizeOutBuffer(0x10000000)
    ResizeModBuffer(0x1000000)
    ChangeInterval(10000000)

    #Pause
    #TracerStartTrace
    TracerStartTraceLight
    #TracerStartTraceDebug
    Return

RE:
    TracerGetExceptionCode
    CheckStrStr(0xc0000004)=(Y:RE_crash)
    CheckStrStr(0xc0000005)=(Y:RE_crash)
    TracerGetExceptionChance
    CheckStrStr(0x00000000)=(Y:RE_crash)
    goto(RE_continue)

RE_crash:
    FlushFiles
    Beep(Xara%20crashed)
    Pause

RE_continue:
    Continue(0x80010001)=

Default:
    Continue=

RC:
    FlushFiles
    Beep(Tracer%20crashed)
    Pause

EN:
    ReadTime
    Beep(Xara%20recording%20finished)
finish:
    FlushFiles
Exception:
    QemuQuit
