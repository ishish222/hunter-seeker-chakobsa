# initial record
Version(2)
Include(scripts/common/initiating.sc)
Include(scripts/common/debugging.sc)

DeployFolders:
    #GlobPattern(_FUZZING_SAMPLES/ableton/crashes/binned_01/0x011e5423/*)
    GlobPattern(_FUZZING_SAMPLES/xara/03_crashes_03/*.xar)
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
#        RunCommand(copy \\10.0.2.4\qemu\input\tmp1ff6wa7z.als c:\users\john\desktop\sample.als)
#        RunCommand(copy \\10.0.2.4\qemu\input\tmp1o02gue4.xar c:\users\john\desktop\sample.xar)
        RunCommand(copy \\10.0.2.4\qemu\input\origin05_7zglca66.xar c:\users\john\desktop\sample.xar)
        CheckStrStr(cannot)=(Y:Main_Sleep)
        CheckStrStr(network)=(Y:Main_Sleep)

    RegisterReactions(kernel32.dll+0x000528fc,CreateFileA_start,0x0;kernel32.dll+0x52942,CreateFileA_end,0x0)
    RegisterReactions(kernel32.dll+0x00050b5d,CreateFileW_start,0x0;kernel32.dll+0x50ba8,CreateFileW_end,0x0)
    EnableReaction(CreateFileA_start)
    EnableReaction(CreateFileW_start)
    DisableReaction(CreateFileA_end)
    DisableReaction(CreateFileW_end)
    AutorepeatReaction(CreateFileA_start)
    AutorepeatReaction(CreateFileW_start)

    RunCommand(start c:\users\john\desktop\sample.xar)
    Continue=

    Main_Sleep:
        Wait(1)
        goto(Main_Copy)

CreateFileA_start:
    ReadArgAnsi(0x1)
    CheckStrStr(sample)=(N:CreateFileA_start_skip)
    EnableReaction(CreateFileA_end)
    CompareCounter(1)=(Y:CreateFileA_start_skip)
    Call(StartAnalysis)
    IncreaseCounter
    
    #ReadFile, need to be separate in order not to fire off each other
    RegisterReactions(kernel32.dll+0x4daab,ReadFile_start,0x0)
    EnableReaction(ReadFile_start)
    AutorepeatReaction(ReadFile_start)

    RegisterReactions(kernel32.dll+0x4db02,ReadFile_end,0x0)
    DisableReaction(ReadFile_end)

    #Because were in it
    EnableReaction(CreateFileA_end)

    CreateFileA_start_skip:

    Continue=

CreateFileA_end:
    ReadRegister(EAX)
    Str
    Enqueue
    PrintQueue
    Continue=
   
CreateFileW_start:
    ReadArgUni(0x1)
    CheckStrStr(sample)=(N:CreateFileW_start_skip)
    EnableReaction(CreateFileW_end)
    CompareCounter(1)=(Y:CreateFileW_start_skip)
    Call(StartAnalysis)
    IncreaseCounter
    
    #ReadFile
    RegisterReactions(kernel32.dll+0x4daab,ReadFile_start,0x0)
    EnableReaction(ReadFile_start)
    AutorepeatReaction(ReadFile_start)

    RegisterReactions(kernel32.dll+0x4db02,ReadFile_end,0x0)
    DisableReaction(ReadFile_end)

    #Because were in it
    EnableReaction(CreateFileW_end)

    CreateFileW_start_skip:

    Continue=

CreateFileW_end:
    ReadRegister(EAX)
    Str
    Enqueue
    PrintQueue
    Continue=

ReadFile_start:
    ReadStack
    ReadRegister(ESP)
    Adjust(0x8)
    Push
    ReadDword
    Str
    Push
    CheckStrStrWQueue=(Y:Interesting)
    Continue=
    
Interesting: 
    ReadStack

    ReadRegister(ESP)
    Adjust(0xc)
    Push
    ReadDword
    Push

    ReadRegister(ESP)
    Adjust(0x10)
    Push
    ReadDword
    Push

    RegisterRegions
    EnableReaction(ReadFile_end)
    Continue=

ReadFile_end:
    TaintRegions
    DisableReaction(ReadFile_end)
    Continue=

StartAnalysis:
    DumpMemory
    SecureAllSections
    AddScannedLocation(ESP:0x50)

#    Call(RegisterEnableBuiltin)

    ResizeOutBuffer(0x10000000)
    ResizeModBuffer(0x1000000)
    ChangeInterval(10000000)

    #Pause
    #TracerStartTrace
    TracerStartTraceLight
    #TracerStartTraceDebug
    TracerDebugContinueInf
    Return

RE:
    TracerGetExceptionCode
    CheckStrStr(0xc0000004)=(Y:RE_crash)
    CheckStrStr(0xc0000005)=(Y:RE_crash)
    TracerGetExceptionChance
    CheckStrStr(0x00000000)=(Y:RE_crash)
    goto(RE_continue)

RE_crash:
    goto(finish)

RE_continue:
    Continue(0x80010001)=

Default:
    Continue=

RC:
    FlushFiles
    Beep(Tracer%20crashed)
    Pause
EN:
RX:
    ReadTime
finish:
    Beep(Recording%20tainted%20Xara%20finished)
    FlushFiles
    Pause
Exception:
    QemuQuit

