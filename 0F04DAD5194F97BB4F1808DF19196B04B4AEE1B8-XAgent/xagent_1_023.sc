# intervention in CC command parsing - additional flags after checksum

PrintLogo
RegisterSignals(exception)
GetOptions
GetSampleOptions
SetSampleFile(netapi64.dll)
SetResearchDir(e:\samples\shared)
GlobPattern(/home/hs1/malware_samples/netapi64.dll)
SetOutDir(\\10.0.2.4\qemu)
CheckHostDir
RevertClean
EnableLogging
PrepareStats
PreparePipes
GlobMethod
DiskGlob
StartQemuFull
QemuMountDisks

start_controller:
SpawnInternalController
QemuConnectDevSocket
IsSocketConnected=(Y:success,N:fail)

fail:
Wait10
goto(start_controller)

success:
SetSampleFile(rundll32.exe)
SetResearchDir(c:\windows\system32)
KillExplorer
ResetTracers
SpawnTracerController
SpawnTracerFileLog
TracerConfigureSample
TracerConfigureOutDir
TracerConfigureOutPrefix
TracerConfigureInDir
TracerPrepareTrace

SpawnResponder(9999)
Enqueue(Test)
Enqueue(Test4)
Enqueue(Test3)
Enqueue(Test2)
Enqueue(Test1)
Enqueue(Test0)
Enqueue(Test9)
Enqueue(Test8)
Enqueue(Test7)
Enqueue(Test6)
Enqueue(Test5)
Enqueue(Test4)
Enqueue(Test3)
Enqueue(Test2)
Enqueue(Test1)
Enqueue(Test0)
Enqueue(AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA)
Dequeue
NextResponse

TracerRegisterBuiltin
DisableReactions
TracerSetParameters(e:\samples\shared\netapi64.dll,init)
TracerDebugSample
TracerDebugContinueInf

# RR
# od poczatku liba
TracerRegisterReactions(netapi64.dll+0x47a0,ST,0x0)
#TracerRegisterReactions(netapi64.dll+0x158b1,ST,0x0)
# niedlugo przed pobieraniem
#TracerRegisterReactions(self+28e5,ST,0x0)
EnableReaction(ST)

# interventions

TracerRegisterReactions(netapi64.dll+0x15874,flip1,0x102)
EnableReaction(filp1)
AutorepeatReaction(flip1)

TracerRegisterReactions(netapi64.dll+0x15888,flip2,0x102)
EnableReaction(filp2)
AutorepeatReaction(flip2)

TracerRegisterReactions(netapi64.dll+0x15890,flip3,0x102)
EnableReaction(filp3)
AutorepeatReaction(flip3)

TracerRegisterReactions(netapi64.dll+0x15898,flip4,0x102)
EnableReaction(filp4)
AutorepeatReaction(flip4)

TracerRegisterReactions(netapi64.dll+0x158a1,flip5,0x102)
EnableReaction(filp5)
AutorepeatReaction(flip5)

TracerRegisterReactions(netapi64.dll+0x390d,flip6,0x105)
EnableReaction(filp6)
AutorepeatReaction(flip6)

TracerRegisterReactions(netapi64.dll+0x396b,flip7,0x105)
EnableReaction(filp7)
AutorepeatReaction(flip7)

# handle signatures

TracerRegisterReactions(netapi64.dll+0xf3a8,sig1,0x105)
EnableReaction(sig1)
AutorepeatReaction(sig1)

TracerRegisterReactions(netapi64.dll+0xf3b3,sig2,0x105)
EnableReaction(sig2)
AutorepeatReaction(sig2)

TracerRegisterReactions(netapi64.dll+0xf3be,sig3,0x105)
EnableReaction(sig3)
AutorepeatReaction(sig3)

TracerRegisterReactions(netapi64.dll+0xf3c9,sig4,0x105)
EnableReaction(sig4)
AutorepeatReaction(sig4)

# handle size setting

TracerRegisterReactions(netapi64.dll+0x112d5,read_arg_3,0x0)
EnableReaction(read_arg_3)
AutorepeatReaction(read_arg_3)

TracerRegisterReactions(netapi64.dll+0x112e2,set_arg_3,0x0)
EnableReaction(set_arg_3)
AutorepeatReaction(set_arg_3)

TracerRegisterReactions(netapi64.dll+0x11319,read_arg_1,0x0)
EnableReaction(read_arg_1)
AutorepeatReaction(read_arg_1)

TracerRegisterReactions(netapi64.dll+0x11320,set_arg_1,0x0)
EnableReaction(set_arg_1)
AutorepeatReaction(set_arg_1)

# handle flag flipping

TracerRegisterReactions(netapi64.dll+0x112e4,flip_query_ret,0x105)
EnableReaction(flip_query_ret)
AutorepeatReaction(flip_query_ret)

# handle checking

TracerRegisterReactions(netapi64.dll+0x1a337,check_int_read,0x0)
EnableReaction(check_int_read)
AutorepeatReaction(check_int_read)

TracerRegisterReactions(netapi64.dll+0x11243,check_send_req,0x0)
EnableReaction(check_send_req)
AutorepeatReaction(check_send_req)

TracerRegisterReactions(netapi64.dll+0x112ed,check_size_read_1,0x0)
EnableReaction(check_size_read_1)
AutorepeatReaction(check_size_read_1)

TracerRegisterReactions(netapi64.dll+0x11337,check_size_read_2,0x0)
EnableReaction(check_size_read_2)
AutorepeatReaction(check_size_read_2)

TracerRegisterReactions(netapi64.dll+0x11341,check_size_read_3,0x0)
EnableReaction(check_size_read_3)
AutorepeatReaction(check_size_read_3)

TracerRegisterReactions(netapi64.dll+0x11323,check_size_read_4,0x0)
EnableReaction(check_size_read_4)
AutorepeatReaction(check_size_read_4)

TracerRegisterReactions(netapi64.dll+0x11362,check_buffer,0x0)
EnableReaction(check_buffer)
AutorepeatReaction(check_buffer)

Execute(scripts/common/enable_internet_detection.sc)

TracerDebugContinueInf
goto(decision)


Start:
    DumpMemory
    SecureAllSections
    EnableBuiltin
    ExclusiveBuiltin
    LowerBuiltin
    EnableReaction(s0)
    RaiseReaction(s0)
    RaiseReaction(s1)
    EnableReaction(SHELLEXEC+2)
    
    Execute(scripts/common/enable_context_mod_detection.sc)

    # we dont need for now, we pass by first creation
    TracerStartTrace
    TracerDebugContinueInf

decision:
Decision=(
    ST:Start,
    SHELLEXEC+2:inspect_shellexecute,
    RE:re,
    INTERNETCONNECTW+1:overwrite_address,
    INTERNETCONNECTW-1:internetopen,
    HTTPSENDREQUESTW+1:HTTPSENDREQUESTW+1,
    HTTPSENDREQUESTW-1:httpsend,
    INTERNETSETOPTION+1:disable_ssl,
    HTTPOPENREQUESTW+1:disable2,
    HTTPOPENREQUESTA+1:disable2,
    HTTPQUERYINFOW+1:get_info1,
    HTTPQUERYINFOW-1:get_info2,
    INTERNETREADFILE+1:get_info3,
    INTERNETREADFILE-1:get_info4,
    update_content_len:update_content_len,
    read_arg_3:read_arg_3,
    read_arg_1:read_arg_1,
    set_arg_3:set_arg_3,
    set_arg_1:set_arg_1,
    check_int_read:check_int_read,
    check_send_req:check_send_req,
    check_size_read_1:check_size_read_1,
    check_size_read_2:check_size_read_2,
    check_size_read_3:check_size_read_3,
    check_size_read_4:check_size_read_4,
    check_buffer:check_buffer,
    default:loop
)

check_size_read_3:
    ReadRegister(EBX)
check_send_req:
check_size_read_1:
check_size_read_2:
check_size_read_4:
check_buffer:
check_int_read:
    ReadRegister(EAX)
    #Pause
    TracerDebugContinueInf
    goto(decision)

read_arg_1:
    ReadStack
    ReadRegister(ESP)
    Adjust(0x0)
    ReadDword
    Push
    TracerDebugContinueInf
    goto(decision)

set_arg_1:
    WriteDword(0x109)
    TracerDebugContinueInf
    goto(decision)

read_arg_3:
    ReadStack
    ReadRegister(ESP)
    Adjust(0x8)
    ReadDword
    Push
    TracerDebugContinueInf
    goto(decision)

set_arg_3:
    Pop
    WriteDword(0x109)
    TracerDebugContinueInf
    goto(decision)

update_content_len:
    Push(0xd8)
    WriteRegister(EAX)
    TracerDebugContinueInf
    goto(decision)

httpsend:
    ReadRegister(EAX)
    Print
    TracerDebugContinueInf
    goto(decision)

disable_ssl:
    ReadRegister(ESP)
    Adjust(0x8)
    ReadDword
    CheckEqual(0x1f)=(Y:disable,N:ignore)

disable:
    ReadRegister(ESP)
    Adjust(0xc)
    ReadDword
    WriteDword(0x0)

ignore:
    TracerDebugContinueInf
    goto(decision)

disable2:
    ReadRegister(ESP)
    Adjust(0x1c)
    WriteDword(0x0)
    TracerDebugContinueInf
    goto(decision)

get_info1:
    ReadStack
    ReadRegister(ESP)
    Adjust(0xc)
    ReadDword
    SaveOffset
    ReadRegister(ESP)
    Adjust(0x10)
    ReadDword
    ReadDword
    SaveSize
    TracerDebugContinueInf
    goto(decision)

get_info2:
    OutRegion
    TracerDebugContinueInf
    goto(decision)

get_info3:
    ReadStack
    ReadRegister(ESP)
    Adjust(0x8)
    ReadDword
    SaveOffset
    ReadRegister(ESP)
    Adjust(0xc)
    ReadDword
    SaveSize
    TracerDebugContinueInf
    goto(decision)

get_info4:
    OutRegion
    TracerDebugContinueInf
    goto(decision)

overwrite_address:
    ReadStack
    ReadRegister(ESP)
    Adjust(0x8)
    ReadDword
    WriteUnicode(127.0.0.1)

    ReadRegister(ESP)
    Adjust(0xc)
    #9999
    WriteDword(0x270f)
    #and now port
    ReadStack

    TracerDebugContinueInf
    goto(decision)

internetopen:
    ReadRegister(EAX)
    TracerDebugContinueInf
    goto(decision)


inspect_shellexecute:
    DumpFile(C:\ProgramData\netapi64.dll)
    Beep
    TracerDebugContinueInf
    goto(decision)

overwrite:
    ReadRegister(ESP)
    Adjust(0x4)
    WriteDword(0x1)
    TracerDebugContinueInf
    goto(decision)

overwrite2:
    ReadRegister(ESP)
    Adjust(0x0)
    WriteDword(0xa)
    TracerDebugContinueInf
    goto(decision)

loop:
    TracerDebugContinueInf
    goto(decision)

re:
    TracerDebugContinueInf(0x80010001)
    goto(decision)

exception:
    Interrupt
    RunCmdHost(mkdir -p /mnt/1/output/logs/xagent_1_023)
    RunCmd(copy e:\logs\responder_log.txt \\10.0.2.4\qemu\logs\xagent_1_023)
    RunCmd(copy e:\server\log_*.txt \\10.0.2.4\qemu\logs\xagent_1_023)
    RunCmd(copy e:\logs\init_log.txt \\10.0.2.4\qemu\logs\xagent_1_023)
    RunCmd(copy e:\logs\last_log.txt \\10.0.2.4\qemu\logs\xagent_1_023)
    QemuQuit

