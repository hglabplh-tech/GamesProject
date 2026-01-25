/*
Copyright (c) 2025 Harald Glab-Plhak

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
 */
package io.github.hglabplh_tech.sinking.backend;

import java.util.Objects;

public class ButtonStatus {
    private boolean isProcessed;
    private final ShipPointType shipPointType;
    private final ShipDefines shipDefine;

    public ButtonStatus(boolean isProcessed, ShipPointType pointType, ShipDefines shipDefine) {
        this.isProcessed = isProcessed;
        this.shipPointType = pointType;
        this.shipDefine = shipDefine;
    }

    public boolean isForeign() {
        return this.shipPointType().equals(ShipPointType.FOREIGN);
    }

    public boolean isHome() {
        return this.shipPointType().equals(ShipPointType.HOME);
    }

    public void toggleProcessed() {
        this.isProcessed = !this.isProcessed();
    }

    public boolean equalsComplete(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        ButtonStatus that = (ButtonStatus) o;
        return this.isProcessed() == that.isProcessed()
                && this.shipPointType() == that.shipPointType()
                && this.shipDefine().equals(that.shipDefine());
    }

    public boolean isProcessed() {
        return this.isProcessed;
    }

    public ShipPointType shipPointType() {
        return shipPointType;
    }

    public ShipDefines shipDefine() {
        return shipDefine;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ButtonStatus that = (ButtonStatus) o;
        return  this.shipPointType().equals(that.shipPointType())
                && this.shipDefine().equals(that.shipDefine());
    }

    @Override
    public int hashCode() {
        return Objects.hash(isProcessed(), shipPointType(), shipDefine());
    }

    @Override
    public String toString() {
        return "ButtonStatus{" +
                "isProcessed=" + this.isProcessed() +
                ", shipPointType=" + this.shipPointType() +
                ", shipDefine=" + this.shipDefine() +
                '}';
    }
}
