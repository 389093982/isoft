'use strict'
const merge = require('webpack-merge')
const devEnv = require('./dev.env')

module.exports = merge(devEnv, {
  NODE_ENV: '"testing"',
  CLIENT_ID:'"de425b2326ee1fa24b68"',
  CLIENT_SECRET:'"0fd3686f1e12e59314046a2c144047a63930282c"',
})
