const util = require('util');
const exec = util.promisify(require('child_process').exec);

// Make sure unhandled rejections makes the process exit
process.on('unhandledRejection', up => { throw up; });

function doInstall(directory) {
    return exec('npm ci --no-audit --ignore-scripts --loglevel=error', { cwd: directory })
              .then(obj => { console.log(directory + ':', (obj.stderr.trim() || obj.stdout.trim())); return obj; });
}

/* Runs npm ci inside each directory listed in `directories`.
 * This assumes that each directory includes a package-lock.json file.
 * To speed up the process up to `parallel` installation processes
 * are run concurrently.
 *
 * Returns: Promise that is resolved when installation has finished
 **/
function installDirectories(directories, parallel=10) {
  const consume = () => {
    const directory = directories.pop();
    if(directory) return doInstall(directory).then(consume);
  };

  const promises = [];
  for(let i = 0; i < parallel; ++i)
    promises.push(consume());

  return Promise.all(promises);
}

module.exports = installDirectories;

