# initial record
Version(2)
Include(scripts/common/initiating.sc)
Include(scripts/common/debugging.sc)

Main:
    RegisterSignals(Exception)

    Call(Intro)
    QemuStart
    QemuLoad(ep6)
    Call(ReconfigureDirs)

    Push(0x1f00000)
    SetBase(injected)

    Call(register_all)

    Continue=

register_all:
    RegisterReactions(injected+0x1a24c,selected_command,0x0)
    EnableReaction(selected_command)

    RegisterReactions(injected+0x1fa3a,finalize,0x0)
    DisableReaction(finalize)

    Return


selected_command:
    DisableReaction(selected_command)
    EnableReaction(finalize)

    CurrentTID
    Push
    SetPriorityHigh

    CurrentTID
    Push
    SuspendAllExcept

    DumpMemory
    SecureAllSections

    Call(RegisterEnableBuiltin)

    TracerStartTraceLight
    Continue=

Default:
    Continue=

EXCEPTION:
    Continue(0x80010001)=

Exception:
    Interrupt

RX:
    HostPrint(Finishing)
 
finalize:
finish:
    QemuQuit

