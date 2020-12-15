'use strict';

import worldMap from '../resources/templates/images/world-map-outline.gif'

const React = require('react');
const ReactDOM = require('react-dom');
const client = require('./client');

class Application extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            countries: [],
            x: 0,
            y: 0
        };
        this.img = {};
        this.canvasHeight = 0;
        this.canvasWidth = 0;
        this.mapData = {};
        this.onMouseClick = this.onMouseClick.bind(this);
        this.onMouseMove = this.onMouseMove.bind(this);
    }

    onMouseMove(e) {
        this.setState({ x: e.nativeEvent.offsetX, y: e.nativeEvent.offsetY });
    }

    onMouseClick(e) {
        // Color at this coordinate
        const canvas = document.getElementById("mapCanvas");
        const ctx = canvas.getContext("2d");
        const white = new Uint8ClampedArray([255, 255, 255, 255]);
        const blue = new Uint8ClampedArray([34, 167, 240, 175]);

        let pixelData = new Array();
        let pixelPos = (this.state.y * this.canvasWidth + this.state.x) * 4

        for (let i = 0; i < 4; i++) {
            pixelData[i] = this.mapData.data[pixelPos + i];
        }
        
        if (this.arrayEqauls(pixelData, blue)) {
            this.floodFill(this.state.x, this.state.y, white, blue);
        }
        else {
            this.floodFill(this.state.x, this.state.y, blue, white);
        }
        ctx.putImageData(this.mapData, 0, 0);
    }

    floodFill(x, y, destColor, srcColor) {

        let pixelPos = (y * this.canvasWidth + x) * 4;
        if (!this.matchColor(pixelPos, srcColor)) {
            return
        }
        this.colorPixel(pixelPos, destColor);
        this.floodFill(x, y - 1, destColor, srcColor);
        this.floodFill(x, y + 1, destColor, srcColor);
        this.floodFill(x + 1, y, destColor, srcColor);
        this.floodFill(x - 1, y, destColor, srcColor);

    }

    matchColor(pixelPos, colorArray) {
        let r = this.mapData.data[pixelPos];
        let g = this.mapData.data[pixelPos + 1];
        let b = this.mapData.data[pixelPos + 2];

        return (r == colorArray[0] && g == colorArray[1] && b == colorArray[2]);
    }

    colorPixel(pixelPos, colorArray) {
        for (let i = 0; i < 4; i++) {
            this.mapData.data[pixelPos + i] = colorArray[i];
        }
    }

    arrayEqauls(a, b) {
        if (a.length != b.length) {
            return false;
        }
        for (let i = 0; i < a.length; i++) {
            if (a[i] != b[i]) {
                return false;
            }
        }
        return true;
    }

    componentDidMount() {
        const canvas = document.getElementById("mapCanvas");
        const context = canvas.getContext("2d");
        const img = new Image();
        img.src = worldMap;
        img.onload = () => {
            context.drawImage(img, 0, 0);
            this.canvasHeight = canvas.height;
            this.canvasWidth = canvas.width;
            this.mapData = context.getImageData(0, 0, canvas.width, canvas.height);

        };


        client({ method: 'GET', path: '/api/countries' }).done(response => {
            this.setState({ countries: response.entity._embedded.countries });
        });
    }

    render() {
        const x = this.state.x;
        const y = this.state.y;

        return (
            // https://stackoverflow.com/questions/42182481/getting-mouse-coordinates-in-react-and-jquery
            <div ref="elem" className="container">
                <div>
                    {/* <img onMouseMove={this.onMouseMove} onClick={this.onMouseClick} alt="map of the world" id="img" src={worldMap} width="908" height="455" /> */}
                    <canvas id="mapCanvas" width="1200px" height="601px" onMouseMove={this.onMouseMove} onClick={this.onMouseClick}></canvas>
                </div>
                <h1>Mouse coordinates: {x} {y}</h1>
            </div>
        );
    }
}

ReactDOM.render(
    <Application />,
    document.getElementById('react')
);

const element = <h3>This is a test!</h3>;
ReactDOM.render(
    element,
    document.getElementById('test')
);
