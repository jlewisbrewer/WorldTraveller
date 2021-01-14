"use strict";

import worldMap from "../resources/templates/images/world-map-outline.gif";

const React = require("react");
const ReactDOM = require("react-dom");
const client = require("./client");
const { Set, fromJS, is, Repeat } = require("immutable");

class Application extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      totalArea: 0,
      possibleCoordinates: new Set(),
      selectedCountries: new Set(),
      selectedCountryArea: 0,
      x: 0,
      y: 0,
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
    const canvas = document.getElementById("mapCanvas");
    const ctx = canvas.getContext("2d");
    const white = new Uint8ClampedArray([255, 255, 255, 255]);
    const blue = new Uint8ClampedArray([34, 167, 240, 175]);
    const tempBlue = new Uint8ClampedArray([35, 168, 241, 176]);

    let pixelData = new Array();
    let x = this.state.x;
    let y = this.state.y;
    let pixelPos = this.translateCoordinates(x, y);
    for (let i = 0; i < 4; i++) {
      pixelData[i] = this.mapData.data[pixelPos + i];
    }

    client({
      method: "GET",
      path: "/search?x=" + this.state.x + "&y=" + this.state.y,
    }).done((response) => {
      this.setState({
        possibleCoordinates: response.entity._embedded.coordinates,
      });

      if (this.arrayEqauls(pixelData, blue)) {
        this.floodFill(x, y, tempBlue, blue);
        let possibleCountries = this.findCountries(
          this.state.possibleCoordinates,
          tempBlue
        );

        let definateCountries = possibleCountries.intersect(
          this.state.selectedCountries
        );

        this.floodFill(x, y, blue, tempBlue);

        if (definateCountries) {
          for (let c of definateCountries.values()) {

            for (let i = 0; i < c.get("coordinates").count(); i++) {
              let coord = c.getIn(["coordinates", i]);
              let cX = coord.get("x");
              let cY = coord.get("y");
              this.floodFill(cX, cY, white, blue);
            }
            this.state.selectedCountries = this.state.selectedCountries.delete(c);
          }
        }
      } else {
        this.floodFill(x, y, blue, white);
        // Find possible countries
        let possibleCountries = this.findCountries(
          this.state.possibleCoordinates,
          blue
        );
        // Check to see if it's not already selected
        let definateCountries = possibleCountries.subtract(
          this.state.selectedCountries
        );
        if (definateCountries) {
          for (let c of definateCountries.values()) {

            for (let i = 0; i < c.get("coordinates").count(); i++) {
              let coord = c.getIn(["coordinates", i]);
              let cX = coord.get("x");
              let cY = coord.get("y");
              this.floodFill(cX, cY, blue, white);
            }
            this.state.selectedCountries = this.state.selectedCountries.add(c);
          }
        }
      }
      ctx.putImageData(this.mapData, 0, 0);

      this.state.selectedCountryArea = this.calculateSelectedArea();

      console.log(this.state.selectedCountryArea);
    });
  }

  calculateSelectedArea() {
    let selectedArea = 0;
    this.state.selectedCountries.valueSeq().forEach(c => selectedArea += c.get("landArea"));

    return Math.round(selectedArea/this.state.totalArea *10000)/100;

  }

  translateCoordinates(x, y) {
    return (y * this.canvasWidth + x) * 4;
  }

  findCountries(coordinates, color) {
    let res = new Set();

    for (let i = 0; i < coordinates.length; i++) {
      let pixelPos = this.translateCoordinates(
        coordinates[i].x,
        coordinates[i].y
      );

      if (this.matchColor(pixelPos, color)) {
        let country = coordinates[i].country;
        res = res.add(fromJS(country));
      }
    }
    return res;
  }

  floodFill(x, y, destColor, srcColor) {
    let pixelPos = this.translateCoordinates(x, y);
    if (!this.matchColor(pixelPos, srcColor)) {
      return;
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

    return r == colorArray[0] && g == colorArray[1] && b == colorArray[2];
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

    client({ method: "GET", path: "/totalarea" }).done((response) => {
      this.setState({ totalArea: response.entity });
    })

    // client({ method: "GET", path: "/api/countries" }).done((response) => {
    //   this.setState({ countries: response.entity._embedded.countries });
    // });
  }

  render() {
    const x = this.state.x;
    const y = this.state.y;
    const selectedArea = this.state.selectedCountryArea;

    return (
      // https://stackoverflow.com/questions/42182481/getting-mouse-coordinates-in-react-and-jquery
      <div ref="elem" className="container">
        <div>
          <canvas
            id="mapCanvas"
            width="1200px"
            height="601px"
            onMouseMove={this.onMouseMove}
            onClick={this.onMouseClick}
          ></canvas>
        </div>
        <h3>
          You have visited {selectedArea}% of the world!
        </h3>
      </div>
    );
  }
}

ReactDOM.render(<Application />, document.getElementById("react"));
