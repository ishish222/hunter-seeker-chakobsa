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
    Call(enable_all)

    Continue=

register_all:
    RegisterReactions(injected+0x18c6,command_00,0x0)
    RegisterReactions(injected+0x1a4d,command_01,0x0)
    RegisterReactions(injected+0x18d4,command_02,0x0)
    RegisterReactions(injected+0x194e,command_03,0x0)
    RegisterReactions(injected+0x195c,command_04,0x0)
    RegisterReactions(injected+0x1960,command_05,0x0)
    RegisterReactions(injected+0x1a57,command_06,0x0)
    RegisterReactions(injected+0x14aa,command_07_thread,0x0)
    RegisterReactions(injected+0xc502,command_08_thread,0x0)
    RegisterReactions(injected+0x16b39,command_09_thread,0x0)
    RegisterReactions(injected+0x14867,command_10_thread,0x0)
    RegisterReactions(injected+0x1b32,command_11,0x0)
    RegisterReactions(injected+0x1b40,command_12,0x0)
    RegisterReactions(injected+0x19fa,command_13,0x0)
    RegisterReactions(injected+0x1b9b,command_14,0x0)
    RegisterReactions(injected+0x1bb4,command_15,0x0)
#    RegisterReactions(injected+0x1bc1,command_16,0x0)
#    RegisterReactions(injected+0xeda1,command_18_thread,0x0)
    RegisterReactions(injected+0x19f7,command_21,0x0)
    RegisterReactions(injected+0x1030e,command_22_thread,0x0)
    RegisterReactions(injected+0x1c85,command_23,0x0)
    RegisterReactions(injected+0x1d30,command_26,0x0)
    RegisterReactions(injected+0x1d3d,command_27,0x0)
    RegisterReactions(injected+0x1d4a,command_28,0x0)
    RegisterReactions(injected+0x1d57,command_29,0x0)
#    RegisterReactions(injected+0x2505e,command_30_thread,0x0)
    RegisterReactions(injected+0x1d79,command_32,0x0)
    RegisterReactions(injected+0x1bd5,command_33,0x0)
    RegisterReactions(injected+0x1c0c,command_34,0x0)
    RegisterReactions(injected+0xfa41,command_35_thread,0x0)
    RegisterReactions(injected+0x1973,command_36,0x0)
    RegisterReactions(injected+0x1b8f,command_37,0x0)
    RegisterReactions(injected+0x1ddc,command_38,0x0)
    RegisterReactions(injected+0x1ada,command_39,0x0)
    RegisterReactions(injected+0x155bc,command_44_thread,0x0)
#    RegisterReactions(injected+0x1a24c,command_45_thread,0x0)
    RegisterReactions(injected+0x1de4,command_46,0x0)
#    RegisterReactions(injected+0xacd2,command_47_thread,0x0)
    RegisterReactions(injected+0x19b4,command_48,0x0)
    RegisterReactions(injected+0xc7ef,command_49_thread,0x0)
    RegisterReactions(injected+0x1970,command_50,0x0)

    RegisterReactions(injected+0x1fa3a,finalize,0x0)
    DisableReaction(finalize)

    Return

enable_all:
    EnableReaction(command_00)
    EnableReaction(command_01)
    EnableReaction(command_02)
    EnableReaction(command_03)
    EnableReaction(command_04)
    EnableReaction(command_05)
    EnableReaction(command_06)
    EnableReaction(command_07_thread)
    EnableReaction(command_08_thread)
    EnableReaction(command_09_thread)
    EnableReaction(command_10_thread)
    EnableReaction(command_11)
    EnableReaction(command_12)
    EnableReaction(command_13)
    EnableReaction(command_14)
    EnableReaction(command_15)
#    EnableReaction(command_16)
#    EnableReaction(command_18_thread)
    EnableReaction(command_21)
    EnableReaction(command_22_thread)
    EnableReaction(command_23)
    EnableReaction(command_26)
    EnableReaction(command_27)
    EnableReaction(command_28)
    EnableReaction(command_29)
#    EnableReaction(command_30_thread)
    EnableReaction(command_32)
    EnableReaction(command_33)
    EnableReaction(command_34)
    EnableReaction(command_35_thread)
    EnableReaction(command_36)
    EnableReaction(command_37)
    EnableReaction(command_38)
    EnableReaction(command_39)
    EnableReaction(command_44_thread)
#    EnableReaction(command_45_thread)
    EnableReaction(command_46)
#    EnableReaction(command_47_thread)
    EnableReaction(command_48)
    EnableReaction(command_49_thread)
    EnableReaction(command_50)
    Return

disable_all:
    DisableReaction(command_00)
    DisableReaction(command_01)
    DisableReaction(command_02)
    DisableReaction(command_03)
    DisableReaction(command_04)
    DisableReaction(command_05)
    DisableReaction(command_06)
    DisableReaction(command_07_thread)
    DisableReaction(command_08_thread)
    DisableReaction(command_09_thread)
    DisableReaction(command_10_thread)
    DisableReaction(command_11)
    DisableReaction(command_12)
    DisableReaction(command_13)
    DisableReaction(command_14)
    DisableReaction(command_15)
    DisableReaction(command_16)
    DisableReaction(command_18_thread)
    DisableReaction(command_21)
    DisableReaction(command_22_thread)
    DisableReaction(command_23)
    DisableReaction(command_26)
    DisableReaction(command_27)
    DisableReaction(command_28)
    DisableReaction(command_29)
    DisableReaction(command_30_thread)
    DisableReaction(command_32)
    DisableReaction(command_33)
    DisableReaction(command_34)
    DisableReaction(command_35_thread)
    DisableReaction(command_36)
    DisableReaction(command_37)
    DisableReaction(command_38)
    DisableReaction(command_39)
    DisableReaction(command_44_thread)
    DisableReaction(command_45_thread)
    DisableReaction(command_46)
    DisableReaction(command_47_thread)
    DisableReaction(command_48)
    DisableReaction(command_49_thread)
    DisableReaction(command_50)
    Return

command_00:
command_01:
command_02:
command_03:
command_04:
command_05:
command_06:
command_07_thread:
command_08_thread:
command_09_thread:
command_10_thread:
command_11:
command_12:
command_13:
command_14:
command_15:
command_16:
command_18_thread:
command_21:
command_22_thread:
command_23:
command_26:
command_27:
command_28:
command_29:
command_30_thread:
command_32:
command_33:
command_34:
command_35_thread:
command_36:
command_37:
command_38:
command_39:
command_44_thread:
command_45_thread:
command_46:
command_47_thread:
command_48:
command_49_thread:
command_50:
    Call(disable_all)
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

