function f() {
    try {
        try {
            return "A";
        } finally {
            return "B";
        }
    } finally {
        "C";
    }
}