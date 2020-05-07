'use strict'
const merge = require('webpack-merge')
const prodEnv = require('./prod.env')

module.exports = merge(prodEnv, {
  NODE_ENV: '"development"',
  CLIENT_ID:'"af46aec6d4529d7cd7fd"',
  CLIENT_SECRET:'"26ed56923769e65599573e85bfe76d5e6740c838"',
})
