# initial record
Version(2)
Include(scripts/common/initiating.sc)
Include(scripts/common/debugging.sc)

DeployFolders:
    #GlobPattern(_FUZZING_SAMPLES/ableton/crashes/binned_01/0x018b06c1/*)
    GlobPattern(_FUZZING_SAMPLES/ableton/crashes/binned_01/0x011f1dc3/*)
    HostDeployInputGlob

    Return

Main:
    RegisterSignals(Exception)

    Call(Intro)
    Call(DeployFolders)

    QemuStart
    QemuLoad(loaded)

    GetPIDByMatch(Trial)
    Push
    SetPID

    Call(AttachFileLog)

    Main_Copy:
        #RunCommand(copy \\10.0.2.4\qemu\input\tmp1k6jalr_.als c:\users\john\desktop\sample.als)
        RunCommand(copy \\10.0.2.4\qemu\input\tmp1o02gue4.als c:\users\john\desktop\sample.als)
        CheckStrStr(cannot)=(Y:Main_Sleep)
        CheckStrStr(network)=(Y:Main_Sleep)

    RegisterReactions(kernel32.dll+0x000528fc,Start_CreateFileA_start,0x0)
    RegisterReactions(kernel32.dll+0x00050b5d,Start_CreateFileW_start,0x0)
    EnableReaction(Start_CreateFileA_start)
    EnableReaction(Start_CreateFileW_start)
    AutorepeatReaction(Start_CreateFileA_start)
    AutorepeatReaction(Start_CreateFileW_start)

    RunCommand(start c:\users\john\desktop\sample.als)
    Continue=

    Main_Sleep:
        Wait(1)
        goto(Main_Copy)

Start_CreateFileA_start:
    ReadArgAnsi(0x1)
    CheckStrStr(sample)=(Y:Start_ReadingSampleA,N:DontStartAnalysis)
    Continue=

Start_CreateFileW_start:
    ReadArgUni(0x1)
    CheckStrStr(sample)=(Y:Start_ReadingSampleW,N:DontStartAnalysis)
    Continue=

CreateFileA_start:
    ReadArgAnsi(0x1)
    CheckStrStr(sample)=(Y:ReadingSampleA,N:DontStartAnalysis)
    Continue=

CreateFileW_start:
    ReadArgUni(0x1)
    CheckStrStr(sample)=(Y:ReadingSampleW,N:DontStartAnalysis)
    Continue=

CreateFileA_end:
    ReadRegister(EAX)
    Str
    Enqueue
    PrintQueue
    Continue=
   
CreateFileW_end:
    ReadRegister(EAX)
    Str
    Enqueue
    PrintQueue
    Continue=

ReadFile_end:
    TaintRegions
    DisableReaction(ReadFile_end)
    #Pause

ReadFile_start:
    ReadStack
    ReadRegister(ESP)
    Adjust(0x8)
    Push
    ReadDword
    Str
    Push
    CheckStrStrWQueue=(N:Uninteresting,Y:Interesting)

Uninteresting:
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

DontStartAnalysis:
    Continue=

Start_ReadingSampleA:
    Call(StartAnalysis)

    DisableReaction(Start_CreateFileA_start)
    DisableReaction(Start_CreateFileW_start)
    RegisterReactions(kernel32.dll+0x000528fc,CreateFileA_start,0x0;kernel32.dll+0x52942,CreateFileA_end,0x0)
    RegisterReactions(kernel32.dll+0x00050b5d,CreateFileW_start,0x0;kernel32.dll+0x50ba8,CreateFileW_end,0x0)
    EnableReaction(CreateFileA_start)
    EnableReaction(CreateFileW_start)
    AutorepeatReaction(CreateFileA_start)
    AutorepeatReaction(CreateFileW_start)
    DisableReaction(CreateFileA_end)
    DisableReaction(CreateFileW_end)

    #ReadFile
#    RegisterReactions(kernel32.dll+0x4daa9,ReadFile_start,0x0)
    RegisterReactions(kernel32.dll+0x4daab,ReadFile_start,0x0)
    EnableReaction(ReadFile_start)
    AutorepeatReaction(ReadFile_start)

    RegisterReactions(kernel32.dll+0x4db02,ReadFile_end,0x0)
    DisableReaction(ReadFile_end)

    #Because were in it
    EnableReaction(CreateFileA_end)
    Continue=

Start_ReadingSampleW:
    Call(StartAnalysis)
    DisableReaction(Start_CreateFileA_start)
    DisableReaction(Start_CreateFileW_start)
    RegisterReactions(kernel32.dll+0x000528fc,CreateFileA_start,0x0;kernel32.dll+0x52942,CreateFileA_end,0x0)
    RegisterReactions(kernel32.dll+0x00050b5d,CreateFileW_start,0x0;kernel32.dll+0x50ba8,CreateFileW_end,0x0)
    EnableReaction(CreateFileA_start)
    EnableReaction(CreateFileW_start)
    AutorepeatReaction(CreateFileA_start)
    AutorepeatReaction(CreateFileW_start)
    DisableReaction(CreateFileA_end)
    DisableReaction(CreateFileW_end)

    #ReadFile
#    RegisterReactions(kernel32.dll+0x4daa9,ReadFile_start,0x0)
    RegisterReactions(kernel32.dll+0x4daab,ReadFile_start,0x0)
    EnableReaction(ReadFile_start)
    AutorepeatReaction(ReadFile_start)

    RegisterReactions(kernel32.dll+0x4db02,ReadFile_end,0x0)
    EnableReaction(ReadFile_end)
    AutorepeatReaction(ReadFile_end)

    #Because were in it
    EnableReaction(CreateFileW_end)
    Continue=

ReadingSampleA:
    EnableReaction(CreateFileA_end)
    Continue=

ReadingSampleW:
    EnableReaction(CreateFileW_end)
    Continue=

StartAnalysis:
    DumpMemory
    SecureAllSections
    #AddScannedLocation(ESP:0x50)

#    Call(RegisterEnableBuiltin)

    ResizeOutBuffer(0x10000000)
    ResizeModBuffer(0x1000000)
    ChangeInterval(10000000)

    #Pause
#    ResetTime
    #TracerStartTrace
    TracerStartTraceLight
    #TracerStartTraceDebug
    TracerDebugContinueInf
    Return

RE:
    FlushFiles
#    ReadTime
    Pause
    Continue(0x80010001)=

Default:
    Continue=

Exception:
finish:
    QemuQuit

