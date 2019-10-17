# initial record
Version(2)

Main:
    RegisterSignals(Exception)

    MLInit
    MLSetEpochs(1)
    MLSetInputDir(_LEARNING/_INPUT/als_text_1/short)
    MLSetModelDir(_LEARNING/_MODELS)

    MLLoadModel(model_002_final)

    MLSetInputFilename(Amp-Guitar_Examples.als.longer.txt)
    MLSaveSamples(model_003.txt)

    Main_train:
        MLTrainModelStep
        MLSaveModel(model_003_part)
        MLCheckMore=(Y:Main_train)

    MLSaveModel(model_003_final)
    MLRemoveModel(model_003_part)

    goto(finish)

Exception:
finish:
    HostPrint(Finishing)

