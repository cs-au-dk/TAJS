var fs = require('fs');
var child_process = require('child_process');

fs.readdir("node_modules", function (err, items) {
    for (var i = 0; i < items.length; i++) {
        if (!items[i].startsWith('.') && !items[i].startsWith('@')) {
            var cmd = "npm install @types/" + items[i] + " --no-audit --ignore-scripts --loglevel=error"; //  --no-save
            console.log(cmd);
            try {
                child_process.execSync(cmd, (error, stdout, stderr) => {
                    if (error) {
                        console.error(error);
                        return;
                    }
                    console.log(stdout);
                    //console.log(stderr);
                });
            } catch(e) {
                // ignore
            }
        }
    }
});
