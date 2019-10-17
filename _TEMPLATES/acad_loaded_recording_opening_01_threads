# initial record
Version(2)
Include(scripts/common/initiating.sc)
Include(scripts/common/debugging.sc)

DeployFolders:
    GlobPattern(_FUZZING_SAMPLES/acad/originals_01/*.dwg)
    HostDeployInputGlob

    Return

Main:
    RegisterSignals(Exception)

    Call(Intro)
    Call(DeployFolders)

    QemuStart
    QemuLoad(loaded)

    GetPIDByMatch(acad.exe)
    Push
    SetPID

    Call(AttachFileLog)

    Main_Copy:
        RunCommand(copy \\10.0.2.4\qemu\input\origin01.dwg c:\users\john\desktop\sample.dwg)
        CheckStrStr(cannot)=(Y:Main_Sleep)
        CheckStrStr(network)=(Y:Main_Sleep)

#   ten generuje wyjatki w jakiejs funkcji byc moze przygotowawczej
#    RegisterReactions(accore.dll+0x710a05,ST,0x0)
#    RegisterReactions(accore.dll+0x710a0a,EN,0x0)

    RegisterReactions(accore.dll+0x89599,ST,0x0)
    RegisterReactions(accore.dll+0x89792,EN,0x0)
    EnableReaction(ST)
    EnableReaction(EN)

    RunCommand(start c:\users\john\desktop\sample.dwg)
    Continue=

    Main_Sleep:
        Wait(1)
        goto(Main_Copy)

ST:
    DisableReaction(ST)
    Call(StartAnalysis)
    ResetTime
    Continue=

TO:
#    Pause
    Dequeue
    Push
    ReleaseThread
    Continue=

StartAnalysis:
    #TracerDebugLogEnable
    ListTebs

    CurrentTID
    Push
    SetPriorityHigh

    CurrentTID
    Push
    SuspendAllExcept

    ReleaseThread(0xe24)
    ReleaseThread(0xd00)

    SetDebugTimeout(5000)

    Enqueue(0x00000ad8)
    Enqueue(0x00000adc)
    Enqueue(0x00000ae0)
    Enqueue(0x00000afc)
    Enqueue(0x00000b04)
    Enqueue(0x00000b0c)
    Enqueue(0x00000b10)
    Enqueue(0x00000bc4)
    Enqueue(0x00000bc8)
    Enqueue(0x00000bfc)
    Enqueue(0x00000cb0)
#    Enqueue(0x00000d00)
    Enqueue(0x00000d04)
    Enqueue(0x00000d0c)
    Enqueue(0x00000d34)
    Enqueue(0x00000d3c)
    Enqueue(0x00000d48)
    Enqueue(0x00000dcc)
#    Enqueue(0x00000e24)
    Enqueue(0x00000e9c)
    Enqueue(0x00000ea4)
    Enqueue(0x00000eac)
    Enqueue(0x00000eb0)
    Enqueue(0x00000eb8)
    Enqueue(0x00000ebc)
    Enqueue(0x00000ec4)
    Enqueue(0x00000edc)
    Enqueue(0x00000ef4)
    Enqueue(0x00000ef8)
    Enqueue(0x00000f04)
    Enqueue(0x00000f44)
    Enqueue(0x00000f60)
    Enqueue(0x00000f64)
    Enqueue(0x00000f68)
    Enqueue(0x00000f74)
    Enqueue(0x00000f78)
    Enqueue(0x00000f7c)
    Enqueue(0x00000fa0)
    Enqueue(0x00000fac)
    Enqueue(0x00000fb0)
    Enqueue(0x00000fb4)
    Enqueue(0x00000fbc)
    Enqueue(0x00000fe0)
    Enqueue(0x000004c4)
    Enqueue(0x00000638)
    Enqueue(0x00000450)

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
    Beep(ACAD%20crashed)
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
    Pause
    ReadTime
finish:
    FlushFiles
Exception:
    QemuQuit
