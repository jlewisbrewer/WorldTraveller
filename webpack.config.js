var path = require('path');

module.exports = {
    entry: './src/main/js/app.js',
    devtool: 'source-map',
    watch: true,
    watchOptions: {
        poll: true,
        ignored: /node_modules/
    },

    mode: 'development',
    output: {
        path: __dirname,
        filename: './src/main/resources/static/built/bundle.js'
    },
    module: {
        rules: [
            {
                test: path.join(__dirname, '../'),
                exclude: /(node_modules)/,
                use: [{
                    loader: 'babel-loader',
                    options: {
                        presets: ["@babel/preset-env", "@babel/preset-react"]
                    }
                }]
            },
            {
                test: /\.(gif|png|jpe?g|svg)$/i,
                use: {
                    loader: 'url-loader',
                },
            },
        ],
    }
};