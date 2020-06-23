const fs = require('fs');
const install = require('./install_common.js');
install(fs.readdirSync('.').filter(item => fs.lstatSync(item).isDirectory()), 1);

