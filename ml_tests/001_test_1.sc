# initial record
Version(2)

Main:
    RegisterSignals(Exception)

    MLInit
    MLSetEpochs(3)
    MLSetInputDir(_LEARNING/_INPUT/als_text_1/short)
    #MLSetInputDir(/home/hs1/ml_input/als_text_1)
    MLSetModelDir(_LEARNING/_MODELS)
    #MLSetModelDir(/home/hs1/ml_models)

#    MLLoadModel(learned-3)
    MLSetInputGlob(*.txt)
    MLSaveSamples(test_1.txt)

    Main_train:
        MLTrainModelStep
        MLSaveModel(test_1_part)
        MLCheckMore=(Y:Main_train)

    MLSaveModel(test_1)

    goto(finish)

finish:
Exception:
    HostPrint(Finishing)


