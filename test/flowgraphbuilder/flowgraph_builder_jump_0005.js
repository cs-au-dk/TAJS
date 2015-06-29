l2: {
    l1: {
        try {
            break l1;
        }finally{
            break l2;
        }
    }
    TAJS_assert(false);
}


TAJS_dumpValue('OK');
