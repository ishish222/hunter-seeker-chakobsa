# initial record
Version(2)

Main:
    RegisterSignals(Exception)

    MLInit
    MLSetEpochs(1)
    MLSetInputDir(_LEARNING/_INPUT/als_text_compressed_1/split)
    MLSetModelDir(_LEARNING/_MODELS)

    MLSetInputGlob(*)
    MLSaveSamples(model_005.txt)

    Main_train:
        MLTrainModelStep
        MLSaveModel(model_005_part)
        MLCheckMore=(Y:Main_train)

    MLSaveModel(model_005_compressed)
    MLRemoveModel(model_005_part)

    goto(finish)

Exception:
finish:
    HostPrint(Finishing)

