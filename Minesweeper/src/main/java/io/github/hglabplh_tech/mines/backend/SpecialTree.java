package io.github.hglabplh_tech.mines.backend;

import java.util.Optional;

public class SpecialTree {

    private TreeElement root = new TreeElement()
            .newBuilder()
            .build();

    private TreeElement lastInserted;

    public SpecialTree () {

    }

    public TreeElement initRoot(ButtonPoint rootPoint) {
        TreeElement newRoot = root.copyBuilder()
                .thisPointOpt(rootPoint)
                .build();
        this.root = newRoot;
        this.lastInserted = this.root;
        return this.root;
    }


    public static class TreeElement {
        private ButtonPoint thisPoint;
        private ButtonPoint parentPoint;
        private ButtonPoint leftPoint;
        private ButtonPoint rightPoint;


        public TreeElement() {
        }

        public ButtonPoint getThisPoint() {
            return thisPoint;
        }

        public ButtonPoint getParentPoint() {
            return this.parentPoint;
        }

        public ButtonPoint getLeftPoint() {
            return leftPoint;
        }

        public ButtonPoint getRightPoint() {
            return rightPoint;
        }

        public Builder newBuilder() {
            return new Builder();
        }

        public Builder copyBuilder() {
            return new Builder(this);
        }

        public static class Builder {
            private Optional<ButtonPoint> thisPointOpt = Optional.empty();
            private Optional<ButtonPoint> parentPointOpt = Optional.empty();
            private Optional<ButtonPoint> leftPointOpt = Optional.empty();
            private Optional<ButtonPoint> rightPointOpt = Optional.empty();
            public Builder() {
            }

            public Builder(TreeElement toCopy) {
                this.thisPointOpt = Optional.of(toCopy.getThisPoint());
                this.parentPointOpt = Optional.of(toCopy.getParentPoint());
                this.leftPointOpt = Optional.of(toCopy.getLeftPoint());
                this.rightPointOpt = Optional.of(toCopy.getRightPoint());
            }

            public Builder thisPointOpt(ButtonPoint point) {
                this.thisPointOpt = Optional.of(point);
                return this;
            }

            public Builder parentPointOpt(ButtonPoint point) {
                this.parentPointOpt = Optional.of(point);
                return this;
            }


            public Builder leftPointOpt(ButtonPoint point) {
                this.leftPointOpt = Optional.of(point);
                return this;
            }

            public Builder rightPointOpt(ButtonPoint point) {
                this.rightPointOpt = Optional.of(point);
                return this;
            }

            public TreeElement build() {
                TreeElement element = new TreeElement();
                element.thisPoint = this.thisPointOpt.orElse(null);
                element.parentPoint = this.parentPointOpt.orElse(null);
                element.leftPoint = this.leftPointOpt.orElse(null);
                element.rightPoint = this.rightPointOpt.orElse(null);
                return element;
            }

        }
    }
}
