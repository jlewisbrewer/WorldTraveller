"use strict";
import ReactTooltip from "react-tooltip";
import CountrySVG from "./components/countrysvg.js";
import SvgComponent from "./components/countrysvg.js";

const React = require("react");
const ReactDOM = require("react-dom");
const client = require("./client");
const { Set, fromJS, is, Repeat } = require("immutable");
const d3 = require("d3");

class Application extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      totalArea: 0,
      selectedCountry: null,
      selectedCountryArea: 0,
      selectedCountries: new Set(),
    };
    this.onMouseClick = this.onMouseClick.bind(this);
    this.onMouseOver = this.onMouseOver.bind(this);
  }

  onMouseOver(e) {
    let target = e.target.id;
    try {
      if (target) {
        this.setState({
          selectedCountry: target.replace("_", " ")
        });
        let paths = d3.select("#worldSVG").selectAll("#" + target);
        paths.attr("data-for", "showCountryName");
      }
    } catch (error) {
      console.log(error);
    }
  }

  onMouseClick(e) {
    let countryName = e.target.id;

    if (countryName) {
      client({
        method: "GET",
        path: "/countries/" + countryName,
      }).done((response) => {
        let country = response.entity;

        if (this.selectedCountryContains(country.id)) {
          // unfill it
          let paths = d3.select("#worldMapSVG").selectAll("#" + countryName);

          paths.attr("class", "unselected");

          let selectedCountry = this.state.selectedCountries.find(
            (c) => c.get("id") === country.id
          );
          this.state.selectedCountries = this.state.selectedCountries.delete(
            selectedCountry
          );
          this.setState({
            selectedCountryArea: this.calculateSelectedArea(),
          });
        } else {
          // fill
          this.state.selectedCountries = this.state.selectedCountries.add(
            fromJS(country)
          );

          this.setState({
            selectedCountryArea: this.calculateSelectedArea(),
          });
          let paths = d3.select("#worldMapSVG").selectAll("#" + countryName);

          paths.attr("class", "selected");
        }
      });
    }
  }

  selectedCountryContains(id) {
    let selectedCountry = this.state.selectedCountries.find(
      (c) => c.get("id") === id
    );
    if (selectedCountry) return true;
    return false;
  }

  calculateSelectedArea() {
    let selectedArea = 0;
    this.state.selectedCountries
      .valueSeq()
      .forEach((c) => (selectedArea += c.get("landArea")));

    return Math.round((selectedArea / this.state.totalArea) * 10000) / 100;
  }

  componentDidMount() {
    client({ method: "GET", path: "/totalarea" }).done((response) => {
      this.setState({ totalArea: response.entity });
    });
  }

  render() {
    const selectedArea = this.state.selectedCountryArea;
    const selectedCountry = this.state.selectedCountry;

    return (
      <div className="app">
        <CountrySVG onMouseClick={this.onMouseClick} onMouseOver={this.onMouseOver}/>
        <ReactTooltip>
          {selectedCountry}
        </ReactTooltip>
        <div ref="elem" className="container">
          <h3>You have visited {selectedArea}% of the world!</h3>
        </div>
      </div>
    );
  }
}

ReactDOM.render(<Application />, document.getElementById("react"));
